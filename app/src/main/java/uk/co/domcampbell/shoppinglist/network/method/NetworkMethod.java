package uk.co.domcampbell.shoppinglist.network.method;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import uk.co.domcampbell.shoppinglist.network.ListService;

/**
 * Created by Dominic on 31/12/2017.
 */

@JsonTypeInfo(
        use = JsonTypeInfo.Id.MINIMAL_CLASS,
        include = JsonTypeInfo.As.PROPERTY,
        property = "@class"
)
public interface NetworkMethod {

    void executeWith(ListService listService);

}
