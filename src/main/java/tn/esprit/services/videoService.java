package tn.esprit.services;
import video.api.client.ApiVideoClient;
import video.api.client.api.ApiException;
import video.api.client.api.models.*;

import java.io.File;

public class videoService {

    public videoService(){}
    public static String uploadVideo(String videoPath) {
        ApiVideoClient apiVideoClient = new ApiVideoClient("7nsq6csd20dlFo8D4VroGLECyoq3zSMrD8m3ZI79oKP");
        // if you rather like to use the sandbox environment:
        // ApiVideoClient apiVideoClient = new ApiVideoClient("YOU_SANDBOX_API_KEY", Environment.SANDBOX);

        File myVideoFile = new File(videoPath);

        try {
            Video video = apiVideoClient.videos().create(new VideoCreationPayload().title("my video"));
            video = apiVideoClient.videos().upload(video.getVideoId(), myVideoFile);
            System.out.println(video);
        } catch (ApiException e) {
            System.err.println("Exception when calling AccountApi#get");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getMessage());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
        return videoPath;
    }
    }

