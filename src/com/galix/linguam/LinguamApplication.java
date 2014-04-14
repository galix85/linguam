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


import com.galix.linguam.db.OriginalWordDBAdapter;
import com.galix.linguam.db.TranslationDBAdapter;

import android.app.Application;
import android.content.Context;


public class LinguamApplication extends Application {
	
	public static Context appContext;
	private static LinguamApplication instance = null;
	public static OriginalWordDBAdapter originalWordDB = null;
	public static TranslationDBAdapter translatedWordDB = null;

	
	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		appContext = getApplicationContext();
		originalWordDB = new OriginalWordDBAdapter(appContext);
		translatedWordDB = new TranslationDBAdapter(appContext);
	}
	
	
	public static LinguamApplication getInstance() {
		if(instance != null) {
			return instance;
		} else {
			return new LinguamApplication();
		}
	}

	/**
	 * Returns the application context
	 *
	 * @return application context
	 */
	public static Context getContext() {
	    return appContext;
	}

}

