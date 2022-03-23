package core.screens;

import auxiliary.socket_managers.ChatSocketManager;
import auxiliary.socket_managers.HeartbeatSocketManager;
import core.controllers.BrokerController;
import javafx.application.Platform;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

public class ConnectionBroker extends GenericScreen implements Closeable {
    private BrokerController controller = null;
    private ServerSocket chatInitiator;
    private Socket heartbeatSocket, chatSocket;
    private HeartbeatSocketManager heartbeatManager;
    private ChatSocketManager chatManager;
    private final CreateTask[] tasks;

    @Override
    public void close() throws IOException {
        heartbeatSocket.close();
        chatSocket.close();
    }

    private class CreateTask {
        private final String taskDescription;
        private Runnable taskRunnable;

        private static boolean executionUnsuccessful = false;

        public CreateTask(String taskDescription) {
            this.taskDescription = taskDescription;
        }

        public CreateTask(String taskDescription, Runnable taskRunnable) {
            this(taskDescription);
            this.taskRunnable = taskRunnable;
        }

        public void setTaskRunnable(Runnable taskRunnable) {
            this.taskRunnable = taskRunnable;
        }

        public void execute(){
            if(executionUnsuccessful){
                return;
            }
            controller.logMessage(String.format("Creating %s... ", taskDescription), false);
            taskRunnable.run();
            if(executionUnsuccessful){
                controller.logMessage("error", true);
                controller.brokerError();
            }
            else {
                controller.logMessage("done", true);
                controller.increaseProgress();
            }
        }

        public static void executionFailed() {
            executionUnsuccessful = true;
        }
    }

    public ConnectionBroker(String address) { // connection is coming from this device
        this();

        tasks[0].setTaskRunnable(() -> {
            try {
                heartbeatSocket = new Socket(address, 2000); // create new socket on same port to separate heartbeat pings and text messages
            } catch (IOException e) {
                CreateTask.executionFailed();
                e.printStackTrace();
            }
        });

        tasks[2].setTaskRunnable(() -> { // make retries sleep this thread instead of primary application
            int retry = 0;
            while (retry < 5){
                try {
                    chatSocket = new Socket(address, 2000); // create new socket on same port to separate heartbeat pings and text messages
                    break;
                } catch (IOException e) {
                    retry++;
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    e.printStackTrace();
                }
            }
            if(retry == 5){
                System.out.println("client time: " + System.currentTimeMillis());
                CreateTask.executionFailed();
            }
        });

        for(CreateTask task : tasks){
            task.execute();
        }
    }

    public ConnectionBroker(Socket incomingSocket, ServerSocket binder){ // connection is coming to this device
        this();

        Platform.runLater(() -> stage.setTitle("server socket here"));

        tasks[0].setTaskRunnable(() -> {
            heartbeatSocket = incomingSocket;
        });

        tasks[2].setTaskRunnable(() -> {
            CountDownLatch latch = new CountDownLatch(1);
            new Thread(() -> {
                try {
                    chatSocket = binder.accept();
                } catch (IOException e) {
                    CreateTask.executionFailed();
                    e.printStackTrace();
                }
                latch.countDown();
            }).start();
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        for(CreateTask task : tasks){
            task.execute();
        }
    }

    private ConnectionBroker(){
        super(GenericScreen.class.getResource("/ConnectionBrokerXML.fxml"));

        controller = ((BrokerController) super.controller);

        tasks = new CreateTask[]{
                new CreateTask("heartbeat socket object"),
                new CreateTask("heartbeat manager", () -> heartbeatManager = new HeartbeatSocketManager(heartbeatSocket)),
                new CreateTask("chat connection object"),
                new CreateTask("chat manager", () -> chatManager = new ChatSocketManager(chatSocket))
        };

        controller.setSteps(tasks.length);
    }
}
