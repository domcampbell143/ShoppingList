package uk.co.domcampbell.shoppinglist.network.method;

import java.util.Queue;

/**
 * Created by Dominic on 17/01/2018.
 */

public interface NetworkMethodQueue {

    void queueNetworkMethod(NetworkMethod networkMethod);

    /***
     * Fetch the queue of network methods and remove them from persisted storage
     * @return queue of network methods
     */
    Queue<NetworkMethod> getQueuedMethods();
}
