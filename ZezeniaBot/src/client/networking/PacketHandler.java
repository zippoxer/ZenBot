package client.networking;

import packets.*;

public interface PacketHandler {

    /**
     * Is called when the receiver fails to receive a packet from socket.
     */
    public void onReceiveException(Exception e);

    /**
     * Is called when a packet handler methods throws an error.
     */
    public void onHandleException(Exception e);

    public void handleErrorPacket(ErrorPacket p) throws Exception;

    public void handleCharListPacket(CharListPacket p) throws Exception;

    public void handleAfterLoginPacket(AfterLoginPacket p) throws Exception;

    public void handleMessageChannelPacket(MessageChannelPacket p) throws Exception;

    public void handleOpenChannelPacket(OpenChannelPacket p) throws Exception;

    public void handleLogoutPacket(LogoutPacket p) throws Exception;

    public void handleStatusUpdatePacket(StatusUpdatePacket p) throws Exception;

    public void handleCreatureAppearPacket(CreatureAppearPacket p) throws Exception;

    public void handleCreatureDisappearPacket(CreatureDisappearPacket p) throws Exception;

    public void handleCreatureWalkPacket(CreatureWalkPacket p) throws Exception;

    public void handleAfterWalkPacket(AfterWalkPacket p) throws Exception;

    public void handleMapUpdatePacket(MapUpdatePacket p) throws Exception;

    public void handleTileUpdatePacket(TileUpdatePacket p) throws Exception;

    public void handleTimeoutCheckPacket(TimeoutCheckPacket p) throws Exception;
    
    public void handleUpdateSignsPacket(UpdateSignsPacket p) throws Exception;

    public void handleCancelWalkPacket(CancelWalkPacket p) throws Exception;

    public void handleCreatureFlagUpdatePacket(CreatureFlagUpdatePacket p) throws Exception;

    public void handleAnimatedTextPacket(AnimatedTextPacket p) throws Exception;

    public void handleEffectPacket(EffectPacket p) throws Exception;

    public void handleCancelPacket(CancelPacket p) throws Exception;

    public void handleOpenContainerPacket(OpenContainerPacket p) throws Exception;

    public void handleCloseContainerPacket(CloseContainerPacket p) throws Exception;

    public void handleCancelPathPacket(CancelPathPacket p) throws Exception;
    
    public void handlePeopleUpdatePacket(PeopleUpdatePacket p) throws Exception;
}
