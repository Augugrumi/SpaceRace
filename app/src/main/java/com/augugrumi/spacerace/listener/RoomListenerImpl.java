package com.augugrumi.spacerace.listener;

import android.content.Intent;
import android.util.Log;

import com.augugrumi.spacerace.MainActivity;
import com.augugrumi.spacerace.MapActivity;
import com.augugrumi.spacerace.R;
import com.augugrumi.spacerace.SpaceRace;
import com.augugrumi.spacerace.utility.gameutility.BaseGameUtils;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesStatusCodes;
import com.google.android.gms.games.multiplayer.Invitation;
import com.google.android.gms.games.multiplayer.OnInvitationReceivedListener;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessageReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomStatusUpdateListener;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Marco Zanella
 * @version 0.01
 *          date 15/11/17
 */

public class RoomListenerImpl implements RealTimeMessageReceivedListener,
        RoomStatusUpdateListener, RoomUpdateListener, OnInvitationReceivedListener {

    private MainActivity activity;
    private String mIncomingInvitationId;
    private ArrayList<Participant> mParticipants = null;
    private String mMyId = null;
    private String mRoomId;

    private int response;

    /**
     *
     * @param activity MainActivity
     * @param response must be RC_RESPONSE
     */
    public RoomListenerImpl(MainActivity activity, int response) {
        this.activity = activity;
        this.response = response;
    }

    @Override
    public void onInvitationReceived(Invitation invitation) {
        Log.i("ROOM", "on invitation received " + invitation.getInviter().getDisplayName());
        mIncomingInvitationId = invitation.getInvitationId();
        activity.setIncomingInvitationId(mIncomingInvitationId);
        activity.showPopUpNotification(true, invitation.getInviter().getDisplayName());
    }

    @Override
    public void onInvitationRemoved(String s) {
        if (mIncomingInvitationId!=null && mIncomingInvitationId.equals(s)) {
            mIncomingInvitationId = null;
            activity.showPopUpNotification(false, ""); // This will hide the invitation popup
        }
    }

    @Override
    public void onRealTimeMessageReceived(RealTimeMessage realTimeMessage) {}

    @Override
    public void onRoomConnecting(Room room) {
        Log.i("ROOM", "on room connecting");
        updateRoom(room);
    }

    @Override
    public void onRoomAutoMatching(Room room) {
        Log.i("ROOM", "on room automatching");
        updateRoom(room);
    }

    @Override
    public void onPeerInvitedToRoom(Room room, List<String> list) {
        Log.i("ROOM", "on peer invited to room");
        updateRoom(room);
    }

    @Override
    public void onPeerDeclined(Room room, List<String> list) {
        Log.i("ROOM", "on peer declined");
        updateRoom(room);
    }

    @Override
    public void onPeerJoined(Room room, List<String> list) {
        Log.i("ROOM", "on peer joined");
        updateRoom(room);
    }

    @Override
    public void onPeerLeft(Room room, List<String> list) {
        Log.i("ROOM", "on peer left");
        updateRoom(room);
    }

    @Override
    public void onConnectedToRoom(Room room) {
        //get participants and my ID:
        mParticipants = room.getParticipants();
        mMyId = room.getParticipantId(Games.Players.getCurrentPlayerId(SpaceRace.getgAPIClient()));

        // save room ID if its not initialized in onRoomCreated() so we can leave cleanly before the game starts.
        if(mRoomId==null) {
            mRoomId = room.getRoomId();
        }
    }

    @Override
    public void onDisconnectedFromRoom(Room room) {
        mRoomId = null;
        BaseGameUtils.makeSimpleDialog(activity,
                SpaceRace.getAppContext().getString(R.string.game_problem));
    }

    @Override
    public void onPeersConnected(Room room, List<String> list) {
        Log.i("ROOM", "on peers connected");
        updateRoom(room);
    }

    @Override
    public void onPeersDisconnected(Room room, List<String> list) {
        Log.i("ROOM", "on peers disconnected");
        updateRoom(room);
    }

    @Override
    public void onP2PConnected(String s) {Log.i("ROOM", "on P2P connected");}

    @Override
    public void onP2PDisconnected(String s) {Log.i("ROOM", "on P2P disconnected");}

    @Override
    public void onRoomCreated(int statusCode, Room room) {
        Log.i("ROOM", "on room created");
        if (statusCode != GamesStatusCodes.STATUS_OK) {
            BaseGameUtils.makeSimpleDialog(activity,
                    SpaceRace.getAppContext().getString(R.string.game_problem));
            return;
        } else {
            mRoomId = room.getRoomId();
            showWaitingRoom(room);
            activity.showPopUpNotification(false, "");
        }

    }

    @Override
    public void onJoinedRoom(int statusCode, Room room) {
        Log.i("ROOM", "on room joined");
        if (statusCode != GamesStatusCodes.STATUS_OK) {
            BaseGameUtils.makeSimpleDialog(activity,
                    SpaceRace.getAppContext().getString(R.string.game_problem));
        } else {
            updateRoom(room);
        }
    }

    @Override
    public void onLeftRoom(int i, String s) {Log.i("ROOM", "on room joined");}

    @Override
    public void onRoomConnected(int statusCode, Room room) {
        Log.i("ROOM", "on room connected");
        if (statusCode != GamesStatusCodes.STATUS_OK) {
            BaseGameUtils.makeSimpleDialog(activity,
                    SpaceRace.getAppContext().getString(R.string.game_problem));
        } else {
            updateRoom(room);
        }
        Intent i = new Intent(activity, MapActivity.class);
        activity.startActivity(i);
    }

    private void updateRoom(Room room) {
        if (room != null) {
            mParticipants = room.getParticipants();
        }
    }

    void showWaitingRoom(Room room) {
        // minimum number of players required for our game
        // For simplicity, we require everyone to join the game before we start it
        // (this is signaled by Integer.MAX_VALUE).
        final int MIN_PLAYERS = Integer.MAX_VALUE;
        Intent i = Games.RealTimeMultiplayer.getWaitingRoomIntent(SpaceRace.getgAPIClient(),
                room, MIN_PLAYERS);

        // show waiting room UI
        activity.startActivityForResult(i, response);
    }
}
