/***************************************************************************
 *   Copyright 2005-2009 Last.fm Ltd.                                      *
 *   Portions contributed by Casey Link, Lukasz Wisniewski,                *
 *   Mike Jennings, and Michael Novak Jr.                                  *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 *   This program is distributed in the hope that it will be useful,       *
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         *
 *   GNU General Public License for more details.                          *
 *                                                                         *
 *   You should have received a copy of the GNU General Public License     *
 *   along with this program; if not, write to the                         *
 *   Free Software Foundation, Inc.,                                       *
 *   51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.         *
 ***************************************************************************/
package com.galix.linguam;

import java.util.Locale;

import android.app.AlertDialog;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.Parcelable;
import android.os.RemoteException;
import android.util.Log;

//
//import fm.last.android.LastFm;
//import fm.last.android.activity.Player;
//import fm.last.android.activity.Profile;
//import fm.last.android.db.LastFmDbHelper;
//import fm.last.android.player.IRadioPlayer;
//import fm.last.android.player.RadioPlayerService;
//import fm.last.android.sync.AccountAuthenticatorService;
//import fm.last.api.Session;
//import fm.last.api.WSError;
//import fm.last.util.UrlUtil;

public class LinguamApplication extends Application {

	public Context mCtx;

	private String mRequestedURL;
	
	private static LinguamApplication instance = null;

	public static LinguamApplication getInstance() {
		if(instance != null) {
			return instance;
		} else {
			return new LinguamApplication();
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
	}

	

	/*public void playRadioStation(Context ctx, String url, boolean showPlayer) {
		mCtx = ctx;
		mRequestedURL = url;
		
		Log.i("Last.fm", "Free trial active: " + getSharedPreferences(LastFm.PREFS, 0).getBoolean("lastfm_freetrial", false));
		Log.i("Last.fm", "Free trial plays elapsed: " + getSharedPreferences(LastFm.PREFS, 0).getInt("lastfm_playselapsed", 0));
		Log.i("Last.fm", "Free trial plays remaining: " + getSharedPreferences(LastFm.PREFS, 0).getInt("lastfm_playsleft", 30));
		
		if (getSharedPreferences(LastFm.PREFS, 0).getBoolean("lastfm_freetrial", false) && getSharedPreferences(LastFm.PREFS, 0).getInt("lastfm_playselapsed", 0) == 0) {
			AlertDialog.Builder d = new AlertDialog.Builder(ctx);
			d.setTitle("Start Free Trial");
			d.setMessage("Radio is a subscriber only feature.  Try it now with a free " + getSharedPreferences(LastFm.PREFS, 0).getInt("lastfm_playsleft", 30) + " track trial.");
			d.setIcon(android.R.drawable.ic_dialog_info);
			d.setPositiveButton("Start Trial", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					SharedPreferences.Editor editor = getSharedPreferences(LastFm.PREFS, 0).edit();
					editor.putInt("lastfm_playselapsed", 1);
					editor.commit();
					playRadioStation(mCtx, mRequestedURL, true);
				}
			});
			d.setNegativeButton("Later", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					//Clear the loading indicators
					sendBroadcast(new Intent("fm.last.android.ERROR"));
				}
			});
			try {
				d.show();
			} catch (Exception e) { //If the app isn't currently on-screen, bring it forward and try again
				Intent intent = new Intent ( Intent.ACTION_VIEW, Uri.parse(mRequestedURL));
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity( intent );
			}
		} else if (session != null && session.getKey().length() > 0) {
			final Intent out = new Intent(this, RadioPlayerService.class);
			out.setAction("fm.last.android.PLAY");
			out.putExtra("station", url);
			out.putExtra("session", (Parcelable) session);
			startService(out);
			if (showPlayer) {
				IntentFilter intentFilter = new IntentFilter();
				intentFilter.addAction(RadioPlayerService.STATION_CHANGED);
				intentFilter.addAction("fm.last.android.ERROR");

				BroadcastReceiver statusListener = new BroadcastReceiver() {

					@Override
					public void onReceive(Context context, Intent intent) {
						try {
							unregisterReceiver(this);
						} catch (Exception e) {
							e.printStackTrace(); //Sometimes this can throw an IllegalArgumentException
						}
							
						String action = intent.getAction();
						if (action.equals(RadioPlayerService.STATION_CHANGED)) {
							Intent i = new Intent(LinguamApplication.this, Player.class);
							i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(i);
						} else if (action.equals("fm.last.android.ERROR")) {
							WSError e = intent.getParcelableExtra("error");
							if(e != null) {
								Log.e("Last.fm", "Tuning error: " + e.getMessage());
							}
							presentError(mCtx, e);
						}
					}
				};
				registerReceiver(statusListener, intentFilter);
			}
		} else {
			Intent i = new Intent(mCtx, LastFm.class);
			i.putExtra("station", url);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mCtx.startActivity(i);
		}
	}*/


	@Override
	public void onTerminate() {
		instance = null;
		super.onTerminate();
	}

	/*public void presentError(Context ctx, WSError error) {
		int title = 0;
		int description = 0;

		if(error != null) {
			Log.e("Last.fm", "Received a webservice error during method: " + error.getMethod() + ", message: " + error.getMessage());
			try {
				LinguamApplication.getInstance().tracker.trackEvent("Errors", // Category
						error.getMethod(), // Action
						error.getMessage(), // Label
						0); // Value
			} catch (Exception e) {
				//Google Analytics doesn't appear to be thread safe
			}

			if (error.getMethod().startsWith("radio.")) {
				title = R.string.ERROR_STATION_TITLE;
				switch (error.getCode()) {
				case WSError.ERROR_NotEnoughContent:
					title = R.string.ERROR_INSUFFICIENT_CONTENT_TITLE;
					description = R.string.ERROR_INSUFFICIENT_CONTENT;
					break;
	
				case WSError.ERROR_NotEnoughFans:
					description = R.string.ERROR_INSUFFICIENT_FANS;
					break;
	
				case WSError.ERROR_NotEnoughMembers:
					description = R.string.ERROR_INSUFFICIENT_MEMBERS;
					break;
	
				case WSError.ERROR_NotEnoughNeighbours:
					description = R.string.ERROR_INSUFFICIENT_NEIGHBOURS;
					break;
					
				case WSError.ERROR_RadioUnavailable:
				case WSError.ERROR_AuthenticationFailed:
				case WSError.ERROR_GeoRestricted:
					description = R.string.ERROR_RADIO_UNAVAILABLE;
					break;
					
				case WSError.ERROR_TrialExpired:
					title = R.string.ERROR_TRIAL_EXPIRED_TITLE;
					description = R.string.ERROR_TRIAL_EXPIRED;
					AlertDialog.Builder d = new AlertDialog.Builder(ctx);
					d.setTitle(getResources().getString(title));
					d.setMessage(getResources().getString(description));
					d.setIcon(android.R.drawable.ic_dialog_alert);
					d.setPositiveButton("Subscribe", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							Intent i = new Intent(Intent.ACTION_VIEW);
							i.setData(Uri.parse("http://www.last.fm/subscribe"));
							i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(i);
						}
					});
					d.setNegativeButton("Later", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
						}
					});
					try {
						d.show();
					} catch (Exception e) {
					}
					return;
					
				case WSError.ERROR_Deprecated:
					title = R.string.ERROR_DEPRECATED_TITLE;
					SharedPreferences settings = getSharedPreferences(LastFm.PREFS, 0);
					SharedPreferences.Editor editor = settings.edit();
					if(mRequestedURL.startsWith("lastfm://playlist/")) {
						description = R.string.ERROR_DEPRECATED_PLAYLISTS;
						editor.putBoolean("remove_playlists", true);
					}
					if(mRequestedURL.startsWith("lastfm://usertags/")) {
						description = R.string.ERROR_DEPRECATED_TAGS;
						editor.putBoolean("remove_tags", true);
					}
					if(mRequestedURL.endsWith("/loved")) {
						description = R.string.ERROR_DEPRECATED_LOVED;
						editor.putBoolean("remove_loved", true);
					}
					editor.commit();
					Intent i = new Intent(RadioPlayerService.STATION_CHANGED);
					sendBroadcast(i);
					break;
				}
			}
	
			if (error.getMethod().equals("user.signUp")) {
				title = R.string.ERROR_SIGNUP_TITLE;
				switch (error.getCode()) {
				case WSError.ERROR_InvalidParameters:
					presentError(ctx, getResources().getString(title), error.getMessage());
					return;
	
				}
			}
		}
		
		if (title == 0)
			title = R.string.ERROR_SERVER_UNAVAILABLE_TITLE;

		if (description == 0) {
			if(error != null) {
				switch (error.getCode()) {
				case WSError.ERROR_AuthenticationFailed:
				case WSError.ERROR_InvalidSession:
					title = R.string.ERROR_SESSION_TITLE;
					description = R.string.ERROR_SESSION;
					break;
				case WSError.ERROR_InvalidAPIKey:
					title = R.string.ERROR_UPGRADE_TITLE;
					description = R.string.ERROR_UPGRADE;
					break;
				case WSError.ERROR_SubscribersOnly:
					title = R.string.ERROR_SUBSCRIPTION_TITLE;
					description = R.string.ERROR_SUBSCRIPTION;
					break;
				default:
					presentError(ctx, getResources().getString(title), getResources().getString(R.string.ERROR_SERVER_UNAVAILABLE) + "\n\n" + error.getMethod() + ": " + error.getMessage());
					return;
				}
			} else {
				description = R.string.ERROR_SERVER_UNAVAILABLE;
			}
		}

		presentError(ctx, getResources().getString(title), getResources().getString(description));
	}

	public void presentError(Context ctx, String title, String description) {
		AlertDialog.Builder d = new AlertDialog.Builder(ctx);
		d.setTitle(title);
		d.setMessage(description);
		d.setIcon(android.R.drawable.ic_dialog_alert);
		d.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});
		try {
			d.show();
		} catch (Exception e) {
			Intent intent = new Intent(LinguamApplication.this, Profile.class);
			intent.putExtra("ERROR_TITLE", title);
			intent.putExtra("ERROR_DESCRIPTION", description);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		}
	}*/
	
	
}
