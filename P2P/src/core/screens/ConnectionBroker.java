package core.screens;

import auxiliary.socket_managers.BrokerSocketManager;
import auxiliary.socket_managers.ChatSocketManager;
import auxiliary.socket_managers.GenericSocketManager;
import auxiliary.socket_managers.HeartbeatSocketManager;
import core.controllers.BrokerController;
import javafx.application.Platform;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class ConnectionBroker extends GenericScreen implements Closeable {
    private final BrokerController controller;
    private final CreateTask[] tasks;
    private final AtomicBoolean brokerageInProgress;

    private String remoteAddress;
    private ServerSocket binder;
    private Socket heartbeatSocket, chatSocket;
    private HeartbeatSocketManager heartbeatManager;
    private ChatSocketManager chatManager;

    @Override
    public void close() throws IOException {
        brokerageInProgress.set(false);

        try {
            heartbeatSocket.close();
            chatSocket.close();
            binder.close();
        }
        catch (NullPointerException nullPointerException){
            System.out.println("Sockets not open. Bypassing close()");
        }
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
                Platform.runLater(controller::brokerError);
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

    public ConnectionBroker(String address, int targetPort) { // connection is coming from this device
        this();

        remoteAddress = address;

        tasks[0].setTaskRunnable(() -> {
            //await acceptance from other machine
            Socket brokerSocket;
            try {
                brokerSocket = new Socket(address, targetPort); //send connect request to other device
                if(new BrokerSocketManager(brokerSocket).awaitAccept()){
                    brokerSocket.close();
                }
                else {
                    CreateTask.executionFailed();
                }
            } catch (IOException e) {
                CreateTask.executionFailed();
                e.printStackTrace();
            }

            binder = GenericSocketManager.createServerSocket(2000); //create serverSocket for future transfer connections
        });

        tasks[1].setTaskRunnable(() -> {
            try {
                heartbeatSocket = new Socket(address, targetPort); // create new socket on same port to separate heartbeat pings and text messages
                System.out.println(heartbeatSocket);
            } catch (IOException e) {
                CreateTask.executionFailed();
                e.printStackTrace();
            }
        });

        tasks[3].setTaskRunnable(() -> {
            int retry = 0;
            while (retry < 5){
                try {
                    chatSocket = new Socket(address, targetPort); // create new socket on same port to separate heartbeat pings and text messages
                    System.out.println(chatSocket);
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
                CreateTask.executionFailed();
            }
        });

        new Thread(() -> {
            for(CreateTask task : tasks){
                task.execute();
            }
        }).start();
    }

    public ConnectionBroker(Socket incomingSocket, ServerSocket binder){ // connection is coming to this device
        this();

        remoteAddress = incomingSocket.getInetAddress().getHostAddress();
        //remotePort = incomingSocket.getPort();

        setTitle("server socket here");

        tasks[0].setTaskRunnable(() -> {
            new BrokerSocketManager(incomingSocket).sendAccept();
            try {
                incomingSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            this.binder = binder;
        });

        tasks[1].setTaskRunnable(() -> {
            try {
                heartbeatSocket = binder.accept();
            } catch (IOException e) {
                CreateTask.executionFailed();
                e.printStackTrace();
            }
        });

        tasks[3].setTaskRunnable(() -> {
            try {
                chatSocket = binder.accept();
            } catch (IOException e) {
                CreateTask.executionFailed();
                e.printStackTrace();
            }
        });

        new Thread(() -> {
            for(CreateTask task : tasks){
                task.execute();
            }
        }).start();
    }

    private ConnectionBroker(){
        super(GenericScreen.class.getResource("/ConnectionBrokerXML.fxml"), "sRocket Connection Broker");

        controller = ((BrokerController) super.controller);
        brokerageInProgress = new AtomicBoolean(true);

        tasks = new CreateTask[]{
                new CreateTask("brokered connection"),
                new CreateTask("heartbeat socket object"),
                new CreateTask("heartbeat manager", () -> {
                    heartbeatManager = new HeartbeatSocketManager(heartbeatSocket);
                    new Thread(() -> {
                        while (brokerageInProgress.get()){
                            if(!heartbeatManager.isConnected()){
                                System.err.println("No response from heartbeat socket. Terminating connection");
                                try {
                                    close(); // close() updates brokerageInProgress so that loop will end
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                new ConnectionDashboard();
                            }
                        }
                    }).start();
                }),
                new CreateTask("chat connection object"),
                new CreateTask("chat manager", () -> {
                    chatManager = new ChatSocketManager(chatSocket);
                    brokerageInProgress.set(false);
                    Platform.runLater(() -> new ConnectedPrimary(heartbeatManager, chatManager, binder, remoteAddress));
                }),
        };

        controller.setSteps(tasks.length);
    }

    @Override
    public void beforeLaunch(String title) {
        super.beforeLaunch(title);
        setResizable(false);
    }
}
