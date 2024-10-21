/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class SocialNetworkTest {

    /*
     * TODO: your testing strategies for these methods should go here.
     * See the ic03-testing exercise for examples of what a testing strategy comment looks like.
     * Make sure you have partitions.
     */
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    
    
    // Testcases for guessFollowGraphs function
    @Test
    public void testGuessFollowsGraphEmpty() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(new ArrayList<>());
        
        assertTrue("expected empty graph", followsGraph.isEmpty());
    }
    
    @Test
    public void testGuessFollowsGraphNoMentions() {
        List<Tweet> tweets = List.of(
            new Tweet(1, "arsalan", "I wrote hello world program yesterday", Instant.now()),
            new Tweet(2, "farhan", "I am testing my functions", Instant.now())
        );
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);
        assertTrue("expected empty graph when no mentions", followsGraph.isEmpty());
    }

    @Test
    public void testGuessFollowsGraphSingleMention() {
        List<Tweet> tweets = List.of(
            new Tweet(1, "arsalan", "Hello @farhan", Instant.now())
        );
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);
        assertTrue("expected 'arsalan' to follow 'farhan'", followsGraph.get("arsalan").contains("farhan"));
    }

    @Test
    public void testGuessFollowsGraphMultipleMentions() {
        List<Tweet> tweets = List.of(
            new Tweet(1, "arsalan", "Hello @farhan @saad. Murree chalein?", Instant.now())
        );
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);
        assertTrue("expected 'arsalan' to follow 'farhan'", followsGraph.get("arsalan").contains("farhan"));
        assertTrue("expected 'arsalan' to follow 'saad'", followsGraph.get("arsalan").contains("saad"));
    }

    @Test
    public void testGuessFollowsGraphMultipleTweets() {
        List<Tweet> tweets = List.of(
            new Tweet(1, "arsalan", "Hello @farhan. How are you?", Instant.now()),
            new Tweet(2, "arsalan", "Hi @saad. Its been a long time", Instant.now())
        );
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);
        assertTrue("expected 'arsalan' to follow 'farhan'", followsGraph.get("arsalan").contains("farhan"));
        assertTrue("expected 'arsalan' to follow 'saad'", followsGraph.get("arsalan").contains("saad"));
    }

    
    
    
    // Testcases for influencers function
    
    @Test
    public void testInfluencersEmpty() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        
        assertTrue("expected empty list", influencers.isEmpty());
    }

    @Test
    public void testInfluencersSingleUserNoFollowers() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("arsalan", Set.of());
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertTrue("expected empty list when user has no followers", influencers.isEmpty());
    }

    @Test
    public void testInfluencersSingleUserWithFollowers() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("arsalan", Set.of("farhan"));
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertEquals("expected 'farhan' to be the only influencer", List.of("farhan"), influencers);
    }

    @Test
    public void testInfluencersMultipleInfluencers() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("arsalan", Set.of("farhan", "saad"));
        followsGraph.put("farhan", Set.of("saad"));
        followsGraph.put("saad", Set.of());

        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertEquals("expected 'saad' to be the top influencer", "saad", influencers.get(0));
        assertEquals("expected 'farhan' to be the second influencer", "farhan", influencers.get(1));
    }


}