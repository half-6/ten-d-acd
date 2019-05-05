package com.tend.acd.controller;

import com.tend.acd.ApplicationTests;
import com.tend.acd.Util;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ImageControllerTest  extends ApplicationTests {

    @Test
    public void BVT() throws Exception {
        recognition("M","Malignant");
        //recognition("B","Benign");
    }

    private void recognition(String folderName,String prediction) throws Exception {
        //String folderName = "M";
        for(File f:Util.getResourceFolderFiles(folderName))
        {
            JSONObject example = new JSONObject();
            String base64String = Util.getBase64String(folderName + "/" + f.getName());
            example.put("roi_image_src",base64String);
            example.put("cancer_type","TH");
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post("/api/image/import")
                    .accept(MediaType.APPLICATION_JSON).content(example.toString())
                    .contentType(MediaType.APPLICATION_JSON);
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            MockHttpServletResponse response = result.getResponse();
            String output = response.getContentAsString();
            Util.logger.trace(output);
            assertEquals(HttpStatus.OK.value(), response.getStatus());
            assertTrue(output.contains("\"Prediction\":\""+ prediction +"\""));
        }
    }
}