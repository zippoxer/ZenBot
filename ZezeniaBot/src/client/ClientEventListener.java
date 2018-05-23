package client;

import structures.Status;

public interface ClientEventListener {

    /**
     * Is called when the client unexpectedly fails to do something.
     */
    public void onException(Exception e);

    /**
     * Is called after the server responds to the login. If error is null, the
     * login is successful.
     */
    public void onLoginResponse(String error);

    public void onMessage(Channel ch, String sender, String message);

    public void onChannelOpen(Channel ch);

    public void onLogout();
    
    public void onStatusUpdate(Status oldStatus, Status newStatus);
    
    public void onEntityAppear(Entity e);
    
    public void onEntityDisappear(Entity e);
    
    public void onEntityWalk(Entity e, short direction);
    
    public void onCreatureFlagUpdate(int attackedID, int followedID);
    
    public void onCancel(String message);
    
    public void onContainerOpen(Container container);
    
    public void onContainerClose(Container container);
    
    public void onContainerUpdate(Container container);
}
