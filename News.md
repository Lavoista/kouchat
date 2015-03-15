# News #


## 01.11.2014 - KouChat for Android is now in the Yandex.Store ##

If you don't have access to Google Play, or don't want to use Google Play, you can now get KouChat for Android from the [Yandex.Store](http://store.yandex.com/) instead. This is an Android app store with over 100.000 apps, and growing. It's big in the Russian market, but free for anyone to use. The Yandex.Store is also available for [Jolla devices](https://jolla.zendesk.com/hc/en-us/articles/201440777-How-Do-I-Install-Android-Applications-via-Yandex-). I don't know if KouChat works on a Jolla device, but please let me know if you try :)

I've already been in contact with Yandex support regarding an error I got when uploading my app to their store, and I'm impressed. They fixed it in two days. That's impressive, compared to my experience with Google. First, Google is difficult to get in contact with. They are more like Hollywood - don't contact us, we'll contact you. Second, if you find a bug, prepare to wait. For years. Google really has something to learn from Yandex in this regard.

But anyway, check out KouChat in the Yandex.Store :)


-- Christian Ihle



---



## 26.10.2014 - KouChat for Android v1.0.2 is out! ##

This is a small improvement release.

The only visible change is the support for sending files from file managers and other apps that share files with the `file://` protocol. All apps I have used from Google, and many others, share with the `content://` protocol, which require a database lookup to find the actual file reference. Some other apps don't, and share the actual file reference instead. With previous versions of KouChat you got a message like this:

<a href='http://kouchat.googlecode.com/svn/site/images/kouchat_android_v1.0.1-file-protocol.png'><img src='http://kouchat.googlecode.com/svn/site/images/kouchat_android_v1.0.1-file-protocol.png' alt='New KouChat for Android' /></a>

These file references should now work.

I've also done a lot of cleanup, and begun work on translation support. I don't think anything has broken from the changes, but let me know if you find any issues :)

The changelog:

  * Lots of cleanups regarding singletons and statics.
  * Split code for saving and loading desktop settings out of Android.
  * Added support for sending files from file managers using the `file://` protocol.
  * Started work on translation support.

Get it on Google Play, or download the apk from the downloads section.


-- Christian Ihle



---



## 28.09.2014 - Downloads are back on GitHub ##

GitHub has added downloads again, called [releases](https://github.com/blog/1547-release-your-software) this time. It's been available a little while, but I was not aware of it when Google stopped allowing downloads on Google Code, so I added the downloads to Google Drive. I've never been happy with the usability of Google Drive, so now I've moved all downloads, including the archive, to GitHub.

Links are in the downloads section.


-- Christian Ihle



---



## 17.08.2014 - KouChat for Android v1.0.1 is out! ##

This is a bug fix release.

There have been some crashes reported when the network connection is down, so this release should fix that.

Thanks to those who report crashes, it's very appreciated! I'd like to ask again for more details in the crash report though. The best is a list of steps to reproduce the problem. If you don't know how to reproduce, then it helps with some description of the last thing you did when it happened.

The changelog:

  * Fixed crash when hiding and showing KouChat without a network connection.
  * Added details about missing connections in the ActionBar.

Get it on Google Play, or download the apk from the downloads section.


-- Christian Ihle



---



## 14.06.2014 - KouChat for Android v1.0.0 is out! ##

This is it! Version 1.0.0 is out!

<a href='http://kouchat.googlecode.com/svn/site/images/kouchat_android_v1.0.0_screenshot_tablet_10_main_chat.png'><img src='http://kouchat.googlecode.com/svn/site/images/kouchat_android_v1.0.0_screenshot_tablet_10_main_chat.png' alt='New KouChat for Android' /></a>

I've been thinking about when to go 1.0 for a while. I could have called it 1.0 a long time ago I guess, but in the end I decided to wait until all the main functionality of KouChat was implemented in the Android client.

So what's new since last time? You can now go away and come back! The options are available from the main menu.

New users might be less confused when they start KouChat for the first time, as the default nick name is now "NewUser" instead of an id. That id is actually a "last resort" nick name. KouChat tried to use the user name of the currently logged in user, but on Android that was never set. With "NewUser" as the default, that id should be a less common sight from now on.

There are still improvements to be made though. Most notably the ability to monitor and cancel file transfers. It's still on my list.

The changelog:

  * Changed default nick name to be "NewUser" instead of a numerical id.
  * The away message of another user, if set, is shown as a subtitle in the ActionBar of the private chat.
  * Added support for away mode.

Get it on Google Play, or download the apk from the downloads section.


-- Christian Ihle



---



## 15.04.2014 - KouChat for Android v0.14.0 is out! ##

The big news this time is support for choosing text colors, and for setting the topic.

You can set two colors. One for your own text. And one for the information messages, like away messages, topics and so on. I'm using the [HoloColorPicker](https://github.com/LarsWerkman/HoloColorPicker), which I think both looks good and works good. I've been looking for a color picker for a long time, and I finally found one I liked. I also added preview icons to the settings screen to make it easy to see which colors you have set at the moment.

The topic can be set from the menu, and now displays on a second line in the action bar, instead of on the same line as your nick name. Which should give much more space to see the actual topic on phones.

<a href='http://kouchat.googlecode.com/svn/site/images/kouchat_android_v0.14.0_screenshot_phone_settings_own_color.png'><img src='http://kouchat.googlecode.com/svn/site/images/kouchat_android_v0.14.0_screenshot_phone_settings_own_color.png' alt='Color picker in the settings' width='300' /></a>
<a href='http://kouchat.googlecode.com/svn/site/images/kouchat_android_v0.14.0_screenshot_phone_main_chat.png'><img src='http://kouchat.googlecode.com/svn/site/images/kouchat_android_v0.14.0_screenshot_phone_main_chat.png' alt='New KouChat for Android' width='300' /></a>

I've also enabled a wifi lock. I've read about many users who experience network issues in apps without a wifi lock. My devices don't care about the wifi lock, but I'm enabling it just in case.

And I have to apologize to the Android v2.3.3 users out there who had issues with KouChat v0.13.0. There seems to have been something wrong with the build, which made it crash on startup on devices with that version of Android. I never noticed the issue myself, as I don't have any devices with v2.3.3, and it never happened with the emulator during development. There were three users who sent me an error report on Google Play, but without any description. So I assumed it was a device issue. Out of curiosity I deployed the Google Play build of v0.13.0 to the emulator and it crashed too. The current build of v0.14.0 does not crash in the emulator, so I hope it now works for everyone. I'll be testing all builds that are uploaded to Google Play in the v2.3.3 emulator from now on, just to be sure. And for those who submit errors reports, please add a description of what you did and what happened when it crashed. I appreciate it :)

The changelog:

  * The topic, if set, is shown as subtitle in the ActionBar.
  * Fixed bug where it was possible to get new lines in the nick name settings dialog.
  * Added support for setting the topic.
  * Added high performance wifi lock.
  * Added color picker in the settings to choose own color and info color.
  * Fixed startup crash on Android 2.3.3, I hope.

Get it on Google Play, or download the apk from the downloads section.


-- Christian Ihle



---



## 01.02.2014 - KouChat for Android v0.13.0 is out! ##

The first release of the year is here :)

The focus this time has been minor tweaks to the user interface, and bug fixes.
The tweaks mostly consists of extra padding, and increased font sizes (on tablets).

<a href='http://kouchat.googlecode.com/svn/site/images/kouchat_android_v0.13.0_screenshot_tablet_10_main_chat.png'><img src='http://kouchat.googlecode.com/svn/site/images/kouchat_android_v0.13.0_screenshot_tablet_10_main_chat.png' alt='New KouChat for Android' /></a>

I hoped these changes would get KouChat accepted as a "tablet app" on Google Play, but I failed. The store still displays a warning that this is a "phone app" when visiting from a tablet. D'oh! Tips are welcome!

_04.02.2014 - After I requested a manual review, Google approved KouChat as a tablet app :)_

Google also [deprecated](http://google-opensource.blogspot.no/2013/05/a-change-to-google-code-download-service.html) the downloads here on Google Code, so I'm unable to add any more downloads. As a workaround I have added the latest releases of KouChat and KouChat Android to a Google Drive account. Links to both are available in the new downloads section.

The changelog:

  * Increased the height of list items in the user list to 48dp, as recommended by Google.
  * Increased the width of the user list on tablets.
  * Added padding around the chat area.
  * Increased the font size on tablets.
  * Fixed issue that caused lines in the chat area with smileys to have extra padding.
  * Fixed several crash issues when rotating.
  * Messages are trimmed before added to the chat, to avoid blank lines.
  * Fixed bug where it was possible to remove KouChat notifications by swiping on some devices.

Get it on Google Play, or download the apk from the downloads section.


-- Christian Ihle



---



## 09.12.2013 - KouChat for Android v0.12.0 is out! ##

New in this release is an optional wake lock. You can find it in the settings:

<a href='http://kouchat.googlecode.com/svn/site/images/kouchat_android_v0.12.0_screenshot_tablet_10_settings.png'><img src='http://kouchat.googlecode.com/svn/site/images/kouchat_android_v0.12.0_screenshot_tablet_10_settings.png' alt='KouChat for Android settings' height='500' /></a>

The effect of the wake lock is best described using a screenshot:

<img src='http://kouchat.googlecode.com/svn/site/images/kouchat-wake_lock.png' alt='KouChat desktop showing the effect of using a wake lock on an Android phone.' />

I left the screen off on my phone most of that day to test. I started with the wake lock off. The phone timed out several times until I turned the wake lock on after about an hour. Then it stayed connected for several hours until it became unstable when I disabled the wake lock again.

The wake lock helped a lot on my phone. Not so much on my tablet. As soon as I turn the screen off, it cuts the network (even though I tell it not to in the settings). YMMV.

I also added an updated launcher icon for the Nexus 5. It has an xxhdpi screen, which I already had icons for. But the new Google Experience Launcher on the Nexus 5 uses launcher icons sized xxxhdpi. I don't have access to one of those phones, but hope the new icon looks nice :)

It seems like Google is trying to improve tablet apps more actively these days by adding a "optimized for phones" tag of shame on a lot of apps. Including KouChat. Thankfully it gives me this hint to help me:
"Your layout should make use of the available space on tablets."

Eh, I have no idea what Google is expecting me to do based on that!
Guess I'll be experimenting a bit with fonts and sizes in the next releases. If I'm lucky I'll stumble upon the correct fix to get rid of that message.

Anyways, here's the full changelog:
  * Fixed rare bug where quitting would not log off the network right away.
  * Added option to enable a wake lock, to keep the device from sleeping and losing the network connection.
  * Added xxxhdpi launcher icon for the Nexus 5.
  * Fixed bug where the file reception dialog was unable to find the file transfer request.

Get it on Google Play, or download the apk from the downloads section.


-- Christian Ihle



---



## 14.10.2013 - KouChat for Android v0.11.0 is out! ##

This release finally adds support for receiving files!
You can now send and receive files between all your devices and computers using KouChat.

<a href='http://kouchat.googlecode.com/svn/site/images/kouchat_android_v0.11.0_screenshot_tablet_10_receive_file.png'><img src='http://kouchat.googlecode.com/svn/site/images/kouchat_android_v0.11.0_screenshot_tablet_10_receive_file.png' alt='KouChat for Android file reception' height='500' /></a>

The file transfer work is not finished though. You still can't cancel an ongoing file transfer or monitor the progress from an Android device. That's for another release.

I've also been fixing some memory leaks, which should make everything work a bit smoother on older devices.

And I've registered the domain [www.kouchat.net](http://www.kouchat.net). Nothing there yet, except a redirect here. Maybe I'll set up a more exciting site there later.

Changelog:
  * Fixed several issues leading to memory leaks.
  * Added basic support for receiving files.
  * Upgraded ActionBarSherlock to version 4.4.0.
  * Changed contact information to use the new kouchat.net domain.

Get it on Google Play, or download the apk from the downloads section.


-- Christian Ihle



---



## 28.07.2013 - KouChat for Android v0.10.0 is out! ##

The new modernized user interface is ready! With new high resolution graphics goodness :)

I think it looks a lot better. Judge for yourself, by looking at these before and after screenshots (click to enlarge):

<a href='http://kouchat.googlecode.com/svn/site/images/kouchat_android_v0.9.0_screenshot_main_chat.png'><img src='http://kouchat.googlecode.com/svn/site/images/kouchat_android_v0.9.0_screenshot_main_chat.png' alt='Old KouChat for Android' width='400' /></a>  <a href='http://kouchat.googlecode.com/svn/site/images/kouchat_android_v0.10.0_screenshot_tablet_10_main_chat.png'><img src='http://kouchat.googlecode.com/svn/site/images/kouchat_android_v0.10.0_screenshot_tablet_10_main_chat.png' alt='New KouChat for Android' width='400' /></a>

I originally planned to add support for receiving files this time, but I opted for upgrading the user interface instead. I've been waiting for better handling in IntelliJ IDEA regarding usage of APIs that aren't available on all the versions of Android that KouChat supports (<a href='http://tools.android.com/recent/lintapicheck'>Lint API Check</a>). And now it became available in IntelliJ IDEA 13 (<a href='http://confluence.jetbrains.com/display/IDEADEV/IDEA+13+EAP'>EAP</a>). The Android maven plugin also got support for this, so now my build fails if I use the wrong APIs. Excellent :) So it became a lot safer for me to upgrade the user interface, without worrying about breaking KouChat on e.g. Android 2.3.3.

The smileys that I'm now using are from Gnome 3. Hope you guys don't mind :) The smileys are using the same license as KouChat.

Changelog:

  * Updated to use high resolution icons.
  * Updated to target API 16 (Android 4.1), to get a more modern look.
  * Added ActionBar, using ActionBarSherlock for compatibility with API 10 (Android 2.3.3).
  * Upgraded Robolectric to version 2.1.
  * Added check for null in onDestroy of different activities to avoid NullPointerExceptions.
  * Added support for selecting text in the chat. Android 3.0 and newer only.
  * Stopped the software keyboard from going full screen on some phones in landscape mode.
  * Added support for avoiding automatic text scroll when the input field has lost focus. Click on some text to remove the focus from the input field. Android 3.0 and newer only.

Hope you like the new interface! Get it on Google Play, or download the apk from the downloads section.

-- Christian Ihle



---



## 09.06.2013 - KouChat for Android v0.9.0 is out! ##

Another release is out! This time I've mostly been working on file transfer support. It's not finished yet, but you can now send files. You won't see the progress of the file transfer (until it's completed), and you can not cancel. The receiver can, though. Since receiving files is not implemented yet, you can only send files to users of the desktop client.

And how do you send a file? You use the "share" feature in different Android applications, like the Gallery:

<a href='http://kouchat.googlecode.com/svn/site/images/kouchat_android_v0.9.0_screenshot_send_file_gallery.png'><img src='http://kouchat.googlecode.com/svn/site/images/kouchat_android_v0.9.0_screenshot_send_file_gallery.png' alt='KouChat for Android' height='500' /></a>

I've also added support for devices that require extra permissions to use multicast networking, like the HTC One phone. I don't know how many devices that have that requirement, but if you could not get KouChat to work on your Android device with previous versions, then try this one :)

Changelog:

  * Improved error handling in the private chat.
  * Added basic support for sending files to other users with the "share/send to" feature in Android apps like the Gallery or different file managers.
  * Added wifi as a required feature in the manifest.
  * Added support for HTC One (and probably others), which require extra permissions and a MulticastLock to use multicast.

Get it on Google Play, or download the apk from the downloads section.


-- Christian Ihle



---



## 07.04.2013 - KouChat for Android v0.8.0 is out! ##

I've been working on notifications this time. So now you can see who is currently writing in the main chat, and you will find a notification icon in the notification area. When a new unread message arrives, it will turn green, like the system tray icon in the desktop version.

Changelog:

  * Added support for showing who is currently writing, using a `"*"` next to the nick name in the user list.
  * Increased the width of the user list to make space for the star.
  * Changed the chat service to a foreground service instead of a background service, to avoid it being killed randomly, removing the chat history and losing the connection.
  * Added notification support, with an icon and a status text shown in the notification area. White icon means nothing to notify. Green icon means new unread messages.

Have fun :)
Get it on Google Play, or download the apk from the downloads section.


-- Christian Ihle



---



## 09.02.2013 - KouChat for Android v0.7.0 is out! ##

A short changelog this time:

  * Added support for smileys: :)  :(  :p  :D  ;)  :O  :@  :S  ;(  :$  8)

Hope this brightens up your day :)
Get it on Google Play, or download the apk from the downloads section.


-- Christian Ihle



---



## 15.12.2012 - GitHub removed all downloads ##

GitHub has decided that they don't want to host downloads anymore. I used to have the Android apk-files there, for those unable to install KouChat from Google Play. The files have been moved to the downloads section of this site instead.

I wish they had notified me about the change, instead of just removing the functionality and making me "google" the reason. Anyway, the announcement from GitHub can be read here: https://github.com/blog/1302-goodbye-uploads


-- Christian Ihle



---



## 08.12.2012 - KouChat for Android v0.6.0 is out! ##

Now it's time for Android! This release adds support for private chats, opening links in the browser on click, and a few other small improvements.

Please note: like the desktop version, the license has changed to the LGPL v3.

Changelog:

  * Imported backend code from KouChat v1.2.0 for desktop
  * Changed license from GPL v3 to LGPL v3
  * Added support for private chat
  * Made links clickable in the chat
  * Made sure the text view is scrolled to the bottom when orientation changes
  * Updated the title of the main chat to not mention a topic if the topic is not set
  * Made it easier to see who you are in the user list by making "you" appear in bold text

Get it on Google Play, or download the apk from github. See [Android](Android.md) for links.


-- Christian Ihle



---



## 19.11.2012 - KouChat v1.2.0 is out! ##

It's release time again! Highlights in this release includes the option to choose which network interface to use, prescaled icons that looks much better than the old ones that Java scaled on the fly, simpler commands for file transfers in console mode, and several bug fixes.

Changelog:

  * Improved debug logging
  * Added support for user specified network interface, in the settings
  * Fix for "java.io.IOException: mark/reset not supported" when playing sound on newer versions of Java 6 (u33) and Java 7 (u5)
  * Added the 4 status icons in svg format
  * Added prescaled status icons for the system tray. 16x16px for Windows, 22x22px for KDE and 24x24px for Gnome
  * Updated kou\_shortcut.ico with sizes from 16x16px to 256x256px
  * Updated kou\_shortcut.png to 256x256px
  * Fixed crash when starting KouChat on systems with the clock set to unix epoch, to support devices without a clock battery
  * Fixed several Swing event dispatch thread issues
  * Changed the /cancel command from `/cancel <nick> <file>` to `/cancel <nick> <id>`
  * Changed the /receive command from `/receive <nick> <file>` to `/receive <nick> <id>`
  * Changed the /reject command from `/reject <nick> <file>` to `/reject <nick> <id>`
  * Fixed issue where file transfers were still shown in the /transfers command after a user had logged off without cancelling, rejecting or accepting the transfer
  * Fixed repaint issue with the buttons when returning from the system tray, on some configurations

Enjoy :)

(I haven't forgotten about Android, in case you wonder :)


-- Christian Ihle



---



## 10.06.2012 - KouChat v1.1.0 is out! ##

I noticed something interesting today when I worked on the finishing touches of this release. It's been exactly 3 years, on the day, since v1.0.0 was released! I did write the announcement on 11.06.2009, but it was available for download from 10.06.2009.

Anyway, what's new in v1.1.0 then?
It contains some new functionality based on feature requests, some bug fixes, and a license change from GPL to LGPL. The license change is because people have asked me about using the network features in KouChat as a library. And that's ok with me.

Changelog:

  * Fix for PatternSyntaxException when parsing commands containing regex     meta characters
  * If the main window is minimized to the system tray and the system tray disappears, the window is shown as minimized in the taskbar instead
  * If the system tray is missing, the minimize feature minimizes to the taskbar instead of being deactivated
  * Removed annoying error message if the system tray is missing
  * Added optional balloon notification on new messages and new private messages
  * Changed license to LGPL v3
  * Added support for logging private chats
  * Added support for the startup argument --no-private-chat to disable private chat
  * Added support for the startup argument --always-log to force logging enabled and disable the option to deactivate again
  * Added support for the startup argument --log-location to specify a custom directory to store log files
  * Increased the number of possible simultaneous KouChat instances (with private chat) and file transfers from 10 to 50

Hope you enjoy this new release! My focus is now on the Android version again.


-- Christian Ihle



---



## 04.06.2012 - KouChat for Android v0.5.1 is out! ##

This release adds support for Android 4.0, Ice Cream Sandwich.
Get it on Google Play, or download the apk from github. Links [here](Android.md).

I've also been working on KouChat (desktop) v1.1.0, and hope to release it soon. Stay tuned!


-- Christian Ihle



---



## 29.11.2011 - KouChat goes Android ##

The first release of KouChat for Android is now available on Android Market!
More details [here](Android.md).


-- Christian Ihle



---



## 15.11.2009 - KouInject goes public ##

I've been working on a dependency injection framework for use in KouChat. This code is now released as a new project called [KouInject](http://kouinject.googlecode.com/). Since the project is unknown for everyone except myself, I'll advertise the release here ;)

-- Christian Ihle



---



## 14.06.2009 - The road ahead ##

Like so many other open source developers, I started KouChat because I had an itch to scratch. With 1.0 out the door, a big part of that itch has been scratched.

But I'm not all done yet! I've added a [roadmap](Roadmap.md) to the wiki with my current thoughts for the road ahead. This roadmap is mostly made up of suggestions from other KouChat users. Which is great! Comments and suggestions are welcome :)

-- Christian Ihle



---



## 11.06.2009 - KouChat v1.0.0 is out! ##

It's finally here :D

The great one point zero! This is a big milestone for me. This means I finally finished what I started. I usually think of 1.0 as the first non-beta release of a piece of software. Full of shortcuts, half-baked features, and bugs. Just to get something out as soon as possible. For some strange reason, that wasn't the way KouChat was developed. I don't even remember when it all started, but it was obviously before the release of 0.5.1 in early 2006 at SourceForge. Maybe six months earlier. Or more. Time flies when you're having fun :)

So, finished you ask? Well, not entirely finished, but I'll come back to that some other day. What I meant was that my initial goals for 1.0 had been reached. And then some! I speculated in calling my first public release 1.0, but thought that KouChat needed a couple of more features and a bit more testing first. Then I got bored with Java and started porting KouChat to C++. And I got bored with C++ and ported the improvements back into the Java version. This was all in a time when KouChat was entirely in Norwegian. The code and the user interface.

For 0.6.0 I translated everything to English. After getting 0.6.1 out the door I felt so close to being done that I jumped straight to 0.9.0. That was in 2007! My plan for the 0.9 series was things like private chat, message logging, console mode, UTF-8 support and sounds. Most of which was done a long time ago. Except I never completed the console mode. What was missing was file transfer. I kind of hit a wall there. Didn't know how to do it, so I kept avoiding it, and came up with other features or fixes to do. And I got suggestions from others, so there was always something else to do.

But it came to a point where enough is enough! I had to finish this. And that was the main focus of this release. And putting some finishing touches on things.

OK, enough babbling, here is a short summary of the most important changes:

  * Fixed a bug when canceling an ongoing file transfer
  * Fixed a concurrency bug in classes for sending and receiving messages
  * Changed to using the user name from the operating system as the default nick name if no settings are found
  * Added a new dialog window for showing unexpected errors to the user
  * Added missing commands for file transfer in console mode
  * Added --debug startup option to activate all logging
  * Added tips & tricks from the wiki to the help menu
  * And some fixes and improvements here and there

Have fun, and don't forget to submit those feature requests and bug reports in the issue tracker :)

-- Christian Ihle



---



## 28.12.2008 - KouChat v0.9.9 is out! ##

I released KouChat version 0.9.9 yesterday, 11 months after 0.9.8.

What happened? My plan was to release 1.0 shortly after 0.9.8, because my todo list was getting very short. I didn't really have any big plans for the next release. So I took a break! Then I started to use Vista at work, and noticed that Vista handled networking in a very strange way which KouChat didn't expect.

And then I got suggestions of improvements and feature requests. And I wanted to do a lot of documentation, and testing. And take more breaks! Hey, where did those months go!?

Well, here's what you can expect in this release:

  * Hopefully better handling of the network connection
  * Smileys :)
  * A choice of which look and feel to use
  * Tweaks to the user interface, especially with the GTK+ and Nimbus look and feels
  * Improvements to notifications when a new message arrives
  * Lots of new code documentation
  * Open folder button on the file transfer dialog
  * Support for running KouChat on more advanced local networks with routers
  * Misc tweaks and fixes all over

You might also have noticed that I brushed up the web site with more screenshots and more information.

Have fun :)

-- Christian Ihle