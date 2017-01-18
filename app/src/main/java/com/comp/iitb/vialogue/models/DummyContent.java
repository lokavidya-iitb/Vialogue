package com.comp.iitb.vialogue.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final ArrayList<Slide> ITEMS = new ArrayList<Slide>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, Slide> ITEM_MAP = new HashMap<String, Slide>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    private static void addItem(Slide item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static Slide createDummyItem(int position) {
        return new Slide(String.valueOf(position), "Item " + position, makeDetails(position),SlideType.IMAGE_AUDIO);
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class Slide {
        public final String id;
        public final String content;
        public final String details;
        public final SlideType slideType;


        public Slide(String id, String content, String details, SlideType slideType) {
            this.id = id;
            this.content = content;
            this.details = details;
            this.slideType = slideType;
        }

        @Override
        public String toString() {
            return content;
        }
    }

    public enum SlideType {
        IMAGE_AUDIO,
        VIDEO
    }
}
