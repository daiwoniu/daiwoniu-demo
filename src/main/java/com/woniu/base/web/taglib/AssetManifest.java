package com.woniu.base.web.taglib;

import com.google.common.io.CharStreams;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class AssetManifest {
    private static AssetManifest defaultAssetManifest;

    private Map<String, String> manifest = new HashMap<>();

    public void load() {
        InputStream in = getClass().getResourceAsStream("/assets.manifest");
        if (in == null) {
            return;
        }

        Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
        try {
            for (String line : CharStreams.readLines(reader)) {
                if (line.isEmpty()) {
                    continue;
                }

                String[] parts = line.split("\t");
                if (parts.length < 2) {
                    continue;
                }
                manifest.put(parts[0], parts[1]);
            }
        } catch (IOException e) {
            throw new RuntimeException("load asset manifest failed", e);
        } finally {
            IOUtils.closeQuietly(reader);
        }
    }

    public String rewriteAsset(String path) {
        String digest = manifest.get(path);
        if (digest == null) {
            return path;
        }

        int idx = path.lastIndexOf('.');
        if (idx == -1) {
            return path + "-" + digest;
        }
        return path.substring(0, idx) + "-" + digest + "." + path.substring(idx + 1);
    }

    public static synchronized AssetManifest getDefaultAssetManifest() {
        if (defaultAssetManifest == null) {
            defaultAssetManifest = new AssetManifest();
            defaultAssetManifest.load();
        }
        return defaultAssetManifest;
    }
}
