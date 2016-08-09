package luke.bamboo.client;

import luke.bamboo.message.ResponseMessage;

public interface ClientParse
{
    public void parse(ResponseMessage resp) throws Exception;
}
