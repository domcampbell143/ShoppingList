package uk.co.domcampbell.shoppinglist.network;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.LinkedList;

import uk.co.domcampbell.shoppinglist.network.method.AddListMethod;
import uk.co.domcampbell.shoppinglist.network.method.NetworkMethod;
import uk.co.domcampbell.shoppinglist.network.method.NetworkMethodQueue;

/**
 * Created by Dominic on 17/01/2018.
 */

@RunWith(MockitoJUnitRunner.class)
public class NoConnectionWrapperTest {

    private NoConnectionWrapper mNoConnectionWrapper;

    @Mock
    private ListService mListService;

    @Mock
    private NetworkMethodQueue mQueue;

    @Before
    public void setUp(){
        mNoConnectionWrapper = new NoConnectionWrapper(mListService, mQueue);
    }

    @Test
    public void networkQueueReturnsInvalidMethod(){
        //given
        NetworkMethod method = new AddListMethod(null);
        Mockito.when(mQueue.getQueuedMethods()).thenReturn(new LinkedList<NetworkMethod>(Arrays.asList(method)));

        //when
        mNoConnectionWrapper.setConnected(true);

        //then
        Mockito.verifyZeroInteractions(mListService);
    }
}
