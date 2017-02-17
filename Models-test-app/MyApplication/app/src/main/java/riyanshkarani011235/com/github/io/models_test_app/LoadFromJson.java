package riyanshkarani011235.com.github.io.models_test_app;

/**
 * Created by ironstein on 12/02/17.
 */

import android.util.Log;


import com.comp.iitb.vialogue.models.ParseObjects.models.Project;

public class LoadFromJson {

    String json =
        "{\n" +
        "    \"id\": \"jmvmmmsl\",\n" +
        "    \"is_edited\": true,\n" +
        "    \"children_resources\":[\n" +
        "       {\n" +
        "           \"id\": \"122asdfkj\",\n" +
        "           \"is_edited\": true,\n" +
        "           \"children_resources\": [\n" +
        "               {\n" +
        "                   \"id\": \"sdglkj12\",\n" +
        "                   \"is_edited\": true,\n" +
        "                   \"children_resources\": [\n" +
        "                       {\n" +
        "                           \"id\": \"sldkgj\",\n" +
        "                           \"is_edited\": true,\n" +
        "                           \"children_resources\": [],\n" +
        "                           \"type\": \"fugiat veniam\",\n" +
        "                           \"url\": \"ex\"\n" +
        "                       }\n" +
        "                   ],\n" +
        "                   \"type\": \"pariatur\",\n" +
        "                   \"url\": \"nisi Lorem laborum\"\n" +
        "               },\n" +
        "               {\n" +
        "                   \"id\": \"sdlkj\",\n" +
        "                   \"is_edited\": true,\n" +
        "                   \"children_resources\": [],\n" +
        "                   \"type\": \"pariatur\",\n" +
        "                   \"url\": \"nisi Lorem laborum\"\n" +
        "               }\n" +
        "           ],\n" +
        "           \"type\": \"pariatur\",\n" +
        "           \"url\": \"nisi Lorem laborum\"\n" +
        "       },\n" +
        "       {\n" +
        "           \"id\": \"sldkgj\",\n" +
        "           \"is_edited\": true,\n" +
        "           \"children_resources\": [],\n" +
        "           \"type\": \"fugiat veniam\",\n" +
        "           \"url\": \"ex\"\n" +
        "       },\n" +
        "       {\n" +
        "           \"id\": \"\",\n" +
        "           \"is_edited\": true,\n" +
        "           \"children_resources\": [\n" +
        "               {\n" +
        "                   \"id\": \"sldkgj\",\n" +
        "                   \"is_edited\": true,\n" +
        "                   \"children_resources\": [],\n" +
        "                   \"type\": \"fugiat veniam\",\n" +
        "                   \"url\": \"ex\"\n" +
        "               }\n" +
        "           ],\n" +
        "           \"type\": \"fugiat veniam\",\n" +
        "           \"url\": \"ex\"\n" +
        "       }\n" +
        "    ],\n" +
        "    \"parent_id\": \"hulla\",\n" +
        "    \"name\": \"nulla\",\n" +
        "    \"description\": \"commodo in\",\n" +
        "    \"author\": {\n" +
        "        \"id\": \"sadf\",\n" +
        "        \"is_edited\": false,\n" +
        "        \"children_resources\": [],\n" +
        "        \"first_name\": \"sunt \",\n" +
        "        \"last_name\": \"ex do ut\",\n" +
        "        \"email\": \"sint incididunt adipisicing\"\n" +
        "    },\n" +
        "    \"category\": {\n" +
        "        \"id\": \"dsalkgj\",\n" +
        "        \"is_edited\": false,\n" +
        "        \"children_resources\": [],\n" +
        "        \"name\": \"proident aliqua\"\n" +
        "    },\n" +
        "    \"language\": {\n" +
        "        \"id\": \"KJFLjfldskj\",\n" +
        "        \"is_edited\": false,\n" +
        "        \"children_resources\": [],\n" +
        "        \"name\": \"culpa\"\n" +
        "    },\n" +
        "    \"tags\": [ \"amet irure occaecat\", \"ad\", \"aute non ex sunt\" ],\n" +
        "    \"resolution\": [ 23839789, 45079728 ],\n" +
        "    \"slide_ordering_sequence\": [ -51074185, 66119648, -88279450, -16543683 ],\n" +
        "    \"slides\": [\n" +
        "        {\n" +
        "            \"id\": \"asdlkgj\",\n" +
        "            \"is_edited\": false,\n" +
        "            \"children_resources\": [\n" +
        "                {\n" +
        "                    \"id\": \"sdglkj12\",\n" +
        "                    \"is_edited\": true,\n" +
        "                    \"children_resources\": [\n" +
        "                        {\n" +
        "                            \"id\": \"sldkgj\",\n" +
        "                            \"is_edited\": true,\n" +
        "                            \"children_resources\": [],\n" +
        "                            \"type\": \"fugiat veniam\",\n" +
        "                            \"url\": \"ex\"\n" +
        "                        }\n" +
        "                    ],\n" +
        "                    \"type\": \"pariatur\",\n" +
        "                    \"url\": \"nisi Lorem laborum\"\n" +
        "                },\n" +
        "                {\n" +
        "                    \"id\": \"sdlkj\",\n" +
        "                    \"is_edited\": true,\n" +
        "                    \"children_resources\": [],\n" +
        "                    \"type\": \"pariatur\",\n" +
        "                    \"url\": \"nisi Lorem laborum\"\n" +
        "                }\n" +
        "            ],\n" +
        "            \"project_slide_id\": -30433962,\n" +
        "            \"hyperlinks\": [\"adlgkj\", \"dsalkj44\"]\n" +
        "        }\n" +
        "    ]\n" +
        "}\n";
    public LoadFromJson() {
        try {
            Log.i("------", "----------------");
            Log.i("------", "----------------");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("saving p", "complete");
    }
}
