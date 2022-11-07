package io.github.tadevi.util.flipper;

import android.content.Context;

import com.ddyos.flipper.mmkv.plugin.MMKVDescriptor;
import com.ddyos.flipper.mmkv.plugin.MMKVFlipperPlugin;
import com.facebook.flipper.android.AndroidFlipperClient;
import com.facebook.flipper.core.FlipperClient;
import com.facebook.flipper.plugins.databases.DatabasesFlipperPlugin;
import com.facebook.flipper.plugins.inspector.DescriptorMapping;
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin;
import com.facebook.flipper.plugins.network.FlipperOkhttpInterceptor;
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin;
import com.facebook.flipper.plugins.sharedpreferences.SharedPreferencesFlipperPlugin;
import com.facebook.soloader.SoLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.OkHttpClient;

public class FlipperUtil {
    private static final NetworkFlipperPlugin networkFlipperPlugin = new NetworkFlipperPlugin();
    private static final FlipperOkhttpInterceptor flipperOkHttpInterceptor = new FlipperOkhttpInterceptor(networkFlipperPlugin);

    static void initialize(Context context) {
        Context applicationContext = context.getApplicationContext();
        if (!SoLoader.isInitialized()) {
            SoLoader.init(applicationContext, false);
        }
        FlipperClient client = AndroidFlipperClient.getInstance(applicationContext);
        client.addPlugin(networkFlipperPlugin);
        client.addPlugin(new DatabasesFlipperPlugin(applicationContext));
        client.addPlugin(new SharedPreferencesFlipperPlugin(applicationContext));
        client.addPlugin(new InspectorFlipperPlugin(applicationContext, DescriptorMapping.withDefaults()));
        try {
            // default root dir of MMKV
            File rootDir = new File(context.getFilesDir().getAbsolutePath() + "/mmkv");
            List<MMKVDescriptor> descriptors = new ArrayList<>();
            for (File file : Objects.requireNonNull(rootDir.listFiles())) {
                if (file.getName().endsWith(".crc")) continue;
                descriptors.add(new MMKVDescriptor(file.getName()));
            }
            client.addPlugin(new MMKVFlipperPlugin(descriptors));
        } catch (Throwable e) {
            e.printStackTrace();
        }
        client.start();
    }

    public static void inject(OkHttpClient.Builder builder) {
        builder.addNetworkInterceptor(flipperOkHttpInterceptor);
    }
}
