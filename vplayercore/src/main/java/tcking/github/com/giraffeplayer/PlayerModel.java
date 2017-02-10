package tcking.github.com.giraffeplayer;

import android.view.View;
import android.webkit.URLUtil;

import java.net.URLConnection;

/**
 * Created by shubh on 06-02-2017.
 */

public class PlayerModel {
    private String mPath;
    private String mAudioPath;
    private MediaType mType;
    private View mView;
    public PlayerModel(View view,String audioPath){
        mView = view;
        mAudioPath = audioPath;
        mType = MediaType.VIEW_AUDIO;
    }

    public PlayerModel(String path, String audioPath) {
        mPath = path;
        mAudioPath = audioPath;
    }

    public void setType(MediaType type) {
        mType = type;
    }

    public String getPath() {
        return mPath;
    }

    public void setPath(String mPath) {
        this.mPath = mPath;
    }

    public String getAudioPath() {
        return mAudioPath;
    }

    public void setAudioPath(String mAudioPath) {
        this.mAudioPath = mAudioPath;
    }

    public MediaType getType() {
        if (mPath != null) {
            if (mType == null) {
                switch (getType(mPath)) {
                    case "video":
                        return MediaType.VIDEO;
                    case "image":
                        if (mAudioPath == null) {
                            return MediaType.IMAGE;
                        }
                        return MediaType.IMAGE_AUDIO;
                    default:
                        if (URLUtil.isValidUrl(mPath))
                            return MediaType.URL;
                        else
                            return MediaType.BROKEN;
                }
            } else {
                return mType;
            }
        }
        return MediaType.BROKEN;
    }

    private String getType(String input) {
        String ans = URLConnection.guessContentTypeFromName(input);
        if (ans != null) {
            String types[] = ans.split("/");
            if (types != null) {
                return types[0];
            }
        }
        return "";
    }

    public enum MediaType {
        VIDEO("video"),
        IMAGE("image"),
        AUDIO("audio"),
        IMAGE_AUDIO("image audio"),
        BROKEN("broken"),
        URL("url"),
        VIEW_AUDIO("view audio");

        private final String text;

        /**
         * @param text
         */
        private MediaType(final String text) {
            this.text = text;
        }

        /* (non-Javadoc)
         * @see java.lang.Enum#toString()
         */

        @Override
        public String toString() {
            return text;
        }
    }
}


