package com.gfk.s2s.demo.s2s.constants

object DemoConstants {
    const val demoConfigPreproduction = "https://demo-config-preproduction.sensic.net/s2s-android.json"

    val configArray = mapOf(
        "demo-config-preproduction" to demoConfigPreproduction,
        "demo-config" to "https://demo-config.sensic.net/s2s-android.json"
    )

    const val aodAudioURL = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3"
    const val audioLiveUrl = "https://stream.rockantenne.de/90er-rock/stream/mp3"
    const val vdoVideoUrl = "https://demo-config-preproduction.sensic.net/video/video3.mp4"
    const val liveTimeShiftedVideoURL = "https://ireplay.tv/test/blender.m3u8" //"https://cph-p2p-msl.akamaized.net/hls/live/2000341/test/master.m3u8"
    const val liveImaVideoURL = "https://ireplay.tv/test/blender.m3u8" //"https://mcdn.daserste.de/daserste/de/master.m3u8"
    const val adSourcePreRollUrl = "https://pubads.g.doubleclick.net/gampad/ads?sz=640x480&iu=/124319096/external/single_ad_samples&ciu_szs=300x250&impl=s&gdfp_req=1&env=vp&output=vast&unviewed_position_start=1&cust_params=deployment%3Ddevsite%26sample_ct%3Dlinear&correlator="
    const val adSourcePostRollUrl = "https://pubads.g.doubleclick.net/gampad/ads?sz=640x480&iu=/124319096/external/single_ad_samples&ciu_szs=300x250&impl=s&gdfp_req=1&env=vp&output=vast&unviewed_position_start=1&cust_params=deployment%3Ddevsite%26sample_ct%3Dredirectlinear&correlator="
    const val adVmaPods = "https://pubads.g.doubleclick.net/gampad/ads?sz=640x480&iu=/124319096/external/ad_rule_samples&ciu_szs=300x250&ad_rule=1&impl=s&gdfp_req=1&env=vp&output=vmap&unviewed_position_start=1&cust_params=deployment%3Ddevsite%26sample_ar%3Dpremidpostpod&cmsid=496&vid=short_onecue&correlator="
    const val adPreRollLinearSkippable = "https://pubads.g.doubleclick.net/gampad/ads?sz=640x480&iu=/124319096/external/single_ad_samples&ciu_szs=300x250&impl=s&gdfp_req=1&env=vp&output=vast&unviewed_position_start=1&cust_params=deployment%3Ddevsite%26sample_ct%3Dskippablelinear&correlator="
}