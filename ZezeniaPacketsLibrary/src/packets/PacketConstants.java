package packets;

/**
 * * Holds constant IDs matched with packet names. * Makes the code easier to
 * read.
 *
 * @author Myzreal
 */
public class PacketConstants {
    
    // CLIENT PACKETS
    public static final int LoginPacket = 0x1;
    public static final int WorldLoginPacket = 0x2;
    public static final int RequestMessageChannelPacket = 0x3;
    public static final int WalkSouthPacket = 0x4;
    public static final int WalkWestPacket = 0x5;
    public static final int WalkNorthPacket = 0x6;
    public static final int WalkEastPacket = 0x7;
    public static final int ItemMovePacket = 0xE;
    public static final int ItemUsePacket = 0xF;
    public static final int RequestCloseContainerPacket = 0x10;
    public static final int LookAtPacket = 0x11;
    public static final int RequestCreatureFlagUpdatePacket = 0x12;
    public static final int AttemptLogoutPacket = 0x13;
    public static final int RequestOpenChannelPacket = 0x15;
    public static final int RequestCloseChannelPacket = 0x16;
    public static final int ConfirmConnectionPacket = 0x19;
    public static final int PathPacket = 0x1A;
    public static final int CastSpellPacket = 0x23;
    
    // SERVER PACKETS
    public static final int ErrorPacket = 0x1;
    public static final int CharListPacket = 0x2;
    public static final int AfterLoginPacket = 0x3;
    public static final int MessageChannelPacket = 0x4;
    public static final int MapUpdatePacket = 0x5;
    public static final int CreatureAppearPacket = 0x6;
    public static final int CreatureDisappearPacket = 0x7;
    public static final int CreatureWalkPacket = 0x8;
    public static final int AfterWalkPacket = 0x9;
    public static final int CancelWalkPacket = 0xA;
    public static final int OpenChannelPacket = 0xB;
    public static final int OpenContainerPacket = 0xD;
    public static final int CloseContainerPacket = 0xE;
    public static final int CancelPacket = 0xF;
    public static final int AnimatedTextPacket = 0x10;
    public static final int EffectPacket = 0x11;
    public static final int CreatureFlagUpdatePacket = 0x12;
    public static final int StatusUpdatePacket = 0x14;
    public static final int LogoutPacket = 0x16;
    public static final int PeopleUpdatePacket = 0x18;
    public static final int TimeoutCheckPacket = 0x19;
    public static final int UpdateSignsPacket = 0x1A;
    public static final int TileUpdatePacket = 0x2C;
    public static final int CancelPathPacket = 0x2E;
}
