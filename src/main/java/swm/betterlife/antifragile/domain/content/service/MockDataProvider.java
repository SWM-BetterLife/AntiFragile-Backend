package swm.betterlife.antifragile.domain.content.service;

import java.util.List;
import org.springframework.stereotype.Component;
import swm.betterlife.antifragile.domain.content.entity.Content;
import swm.betterlife.antifragile.domain.content.entity.YoutubeInfo;

// 테스트를 위한 Mock 데이터
@Component
public class MockDataProvider {

    public static List<Content> getContents1() {
        return List.of(
            Content.builder()
                .title("Introduction to Kotlin")
                .description("Learn the basics of Kotlin programming.")
                .thumbnailImgUrl("https://example.com/thumbnail1.jpg")
                .youTubeInfo(YoutubeInfo.builder()
                    .subscriberNumber(120000L)
                    .channelName("Kotlin Academy")
                    .channelImg("https://example.com/channel1.jpg")
                    .viewNumber(180000L)
                    .likeNumber(15000L)
                    .build())
                .appViewNumber(600L)
                .appLikeNumber(60L)
                .url("https://youtube.com/watch?v=111111")
                .build(),
            Content.builder()
                .title("Advanced Kotlin Techniques")
                .description("Take your Kotlin skills to the next level.")
                .thumbnailImgUrl("https://example.com/thumbnail2.jpg")
                .youTubeInfo(YoutubeInfo.builder()
                    .subscriberNumber(220000L)
                    .channelName("Kotlin Mastery")
                    .channelImg("https://example.com/channel2.jpg")
                    .viewNumber(280000L)
                    .likeNumber(25000L)
                    .build())
                .appViewNumber(1200L)
                .appLikeNumber(120L)
                .url("https://youtube.com/watch?v=222222")
                .build(),
            Content.builder()
                .title("Spring Boot in Depth")
                .description("Deep dive into Spring Boot.")
                .thumbnailImgUrl("https://example.com/thumbnail3.jpg")
                .youTubeInfo(YoutubeInfo.builder()
                    .subscriberNumber(320000L)
                    .channelName("Spring Pro")
                    .channelImg("https://example.com/channel3.jpg")
                    .viewNumber(380000L)
                    .likeNumber(35000L)
                    .build())
                .appViewNumber(1800L)
                .appLikeNumber(180L)
                .url("https://youtube.com/watch?v=333333")
                .build(),
            Content.builder()
                .title("Microservices with Spring Cloud")
                .description("Build microservices using Spring Cloud.")
                .thumbnailImgUrl("https://example.com/thumbnail4.jpg")
                .youTubeInfo(YoutubeInfo.builder()
                    .subscriberNumber(420000L)
                    .channelName("Cloud Mastery")
                    .channelImg("https://example.com/channel4.jpg")
                    .viewNumber(480000L)
                    .likeNumber(45000L)
                    .build())
                .appViewNumber(2400L)
                .appLikeNumber(240L)
                .url("https://youtube.com/watch?v=444444")
                .build(),
            Content.builder()
                .title("Reactive Programming with Spring")
                .description("Learn reactive programming with Spring Framework.")
                .thumbnailImgUrl("https://example.com/thumbnail5.jpg")
                .youTubeInfo(YoutubeInfo.builder()
                    .subscriberNumber(520000L)
                    .channelName("Reactive Spring")
                    .channelImg("https://example.com/channel5.jpg")
                    .viewNumber(580000L)
                    .likeNumber(55000L)
                    .build())
                .appViewNumber(3000L)
                .appLikeNumber(300L)
                .url("https://youtube.com/watch?v=555555")
                .build()
        );
    }

    public static List<Content> getContents2() {
        return List.of(
            Content.builder()
                .title("Introduction to Kotlin")
                .description("Learn the basics of Kotlin programming.")
                .thumbnailImgUrl("https://example.com/thumbnail111.jpg")
                .youTubeInfo(YoutubeInfo.builder()
                    .subscriberNumber(120000L)
                    .channelName("Kotlin Academy")
                    .channelImg("https://example.com/channel111.jpg")
                    .viewNumber(180000L)
                    .likeNumber(15000L)
                    .build())
                .appViewNumber(600L)
                .appLikeNumber(60L)
                .url("https://youtube.com/watch?v=111111")
                .build(),
            Content.builder()
                .title("Advanced Kotlin Techniques")
                .description("Take your Kotlin skills to the next level.")
                .thumbnailImgUrl("https://example.com/thumbnail222.jpg")
                .youTubeInfo(YoutubeInfo.builder()
                    .subscriberNumber(220000L)
                    .channelName("Kotlin Mastery")
                    .channelImg("https://example.com/channel2.jpg")
                    .viewNumber(280000L)
                    .likeNumber(25000L)
                    .build())
                .appViewNumber(1200L)
                .appLikeNumber(120L)
                .url("https://youtube.com/watch?v=222222")
                .build(), // Advanced Kotlin Techniques
            Content.builder()
                .title("Docker for Beginners")
                .description("Learn Docker from scratch.")
                .thumbnailImgUrl("https://example.com/thumbnail6.jpg")
                .youTubeInfo(YoutubeInfo.builder()
                    .subscriberNumber(620000L)
                    .channelName("Docker Academy")
                    .channelImg("https://example.com/channel6.jpg")
                    .viewNumber(680000L)
                    .likeNumber(65000L)
                    .build())
                .appViewNumber(3600L)
                .appLikeNumber(360L)
                .url("https://youtube.com/watch?v=666666")
                .build(),
            Content.builder()
                .title("CI/CD with Jenkins")
                .description("Implement CI/CD pipelines using Jenkins.")
                .thumbnailImgUrl("https://example.com/thumbnail17.jpg")
                .youTubeInfo(YoutubeInfo.builder()
                    .subscriberNumber(720000L)
                    .channelName("Jenkins Mastery")
                    .channelImg("https://example.com/channel17.jpg")
                    .viewNumber(780000L)
                    .likeNumber(75000L)
                    .build())
                .appViewNumber(4200L)
                .appLikeNumber(420L)
                .url("https://youtube.com/watch?v=777777")
                .build(),
            Content.builder()
                .title("AWS Cloud Essentials")
                .description("Get started with AWS Cloud.")
                .thumbnailImgUrl("https://example.com/thumbnail18.jpg")
                .youTubeInfo(YoutubeInfo.builder()
                    .subscriberNumber(820000L)
                    .channelName("AWS Academy")
                    .channelImg("https://example.com/channel18.jpg")
                    .viewNumber(880000L)
                    .likeNumber(85000L)
                    .build())
                .appViewNumber(4800L)
                .appLikeNumber(480L)
                .url("https://youtube.com/watch?v=888888")
                .build()
        );
    }

    public static List<Content> getContents3() {
        return List.of(
            Content.builder()
                .title("Git and GitHub for Beginners")
                .description("Learn how to use Git and GitHub.")
                .thumbnailImgUrl("https://example.com/thumbnail9.jpg")
                .youTubeInfo(YoutubeInfo.builder()
                    .subscriberNumber(920000L)
                    .channelName("Git Academy")
                    .channelImg("https://example.com/channel9.jpg")
                    .viewNumber(980000L)
                    .likeNumber(95000L)
                    .build())
                .appViewNumber(5400L)
                .appLikeNumber(540L)
                .url("https://youtube.com/watch?v=999999")
                .build(),
            Content.builder()
                .title("Introduction to Machine Learning")
                .description("Basics of machine learning explained.")
                .thumbnailImgUrl("https://example.com/thumbnail10.jpg")
                .youTubeInfo(YoutubeInfo.builder()
                    .subscriberNumber(1020000L)
                    .channelName("ML Mastery")
                    .channelImg("https://example.com/channel10.jpg")
                    .viewNumber(1080000L)
                    .likeNumber(105000L)
                    .build())
                .appViewNumber(6000L)
                .appLikeNumber(600L)
                .url("https://youtube.com/watch?v=10-10-10")
                .build(),
            Content.builder()
                .title("Deep Learning with TensorFlow")
                .description("Advanced deep learning techniques using TensorFlow.")
                .thumbnailImgUrl("https://example.com/thumbnail11.jpg")
                .youTubeInfo(YoutubeInfo.builder()
                    .subscriberNumber(1120000L)
                    .channelName("TensorFlow Academy")
                    .channelImg("https://example.com/channel111.jpg")
                    .viewNumber(1180000L)
                    .likeNumber(115000L)
                    .build())
                .appViewNumber(6600L)
                .appLikeNumber(660L)
                .url("https://youtube.com/watch?v=11-11-11")
                .build(),
            Content.builder()
                .title("Kubernetes for Beginners")
                .description("Learn Kubernetes from the ground up.")
                .thumbnailImgUrl("https://example.com/thumbnail12jpg")
                .youTubeInfo(YoutubeInfo.builder()
                    .subscriberNumber(1220000L)
                    .channelName("Kubernetes Academy")
                    .channelImg("https://example.com/channel12.jpg")
                    .viewNumber(1280000L)
                    .likeNumber(125000L)
                    .build())
                .appViewNumber(7200L)
                .appLikeNumber(720L)
                .url("https://youtube.com/watch?v=12-12-12")
                .build(),
            Content.builder()
                .title("CI/CD with GitHub Actions")
                .description("Implement CI/CD pipelines using GitHub Actions.")
                .thumbnailImgUrl("https://example.com/thumbnail13.jpg")
                .youTubeInfo(YoutubeInfo.builder()
                    .subscriberNumber(1320000L)
                    .channelName("GitHub Actions Mastery")
                    .channelImg("https://example.com/channel13.jpg")
                    .viewNumber(1380000L)
                    .likeNumber(135000L)
                    .build())
                .appViewNumber(7800L)
                .appLikeNumber(780L)
                .url("https://youtube.com/watch?v=13-13-13")
                .build()
        );
    }
}