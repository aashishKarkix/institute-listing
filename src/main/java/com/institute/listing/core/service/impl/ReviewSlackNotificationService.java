package com.institute.listing.core.service.impl;

import com.institute.listing.core.model.Institution;
import com.institute.listing.core.model.Review;
import com.institute.listing.core.model.User;
import com.institute.listing.core.service.SlackService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewSlackNotificationService {

    private final SlackService slackService;

    @Async
    public void sendReviewNotification(User user, Institution institution, Review newReview, Review oldReview, String action) {
        slackService.sendMessage(buildMessage(user, institution, newReview, oldReview, action));
    }

    private String buildMessage(User user, Institution institution, Review newReview, Review oldReview, String action) {
        return switch (action) {
            case "created" -> """
                %s added a new review for %s
                • Rating: %.1f
                • Comment: %s
                """.formatted(user.getName(), institution.getName(),
                    newReview.getRating(), newReview.getComment());
            case "updated" -> """
                *%s* updated a review for *%s*:
                • *Old Rating:* %.1f
                • *Old Comment:* %s
                • *New Rating:* %.1f
                • *New Comment:* %s
                """.formatted(user.getName(), institution.getName(),
                    oldReview.getRating(), oldReview.getComment(),
                    newReview.getRating(), newReview.getComment());
            case "deleted" -> """
                *%s* deleted a review for *%s*:
                • *Rating:* %.1f
                • *Comment:* %s
                """.formatted(user.getName(), institution.getName(),
                    oldReview.getRating(), oldReview.getComment());
            default -> throw new IllegalArgumentException("Unknown Slack action: " + action);
        };
    }

}
