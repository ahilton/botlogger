package org.bttf.botlogger;

import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Lists;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class BotloggerController {

    private final MutableList<String> conversationIdList = Lists.mutable.of();

    @GetMapping("/conversation/log")
    @ResponseBody
    public void logConversationId(String conversationId){
        conversationIdList.add(conversationId);
    }

    @GetMapping("/conversation/last")
    @ResponseBody
    public String getLastConversationId(){
        if (conversationIdList.isEmpty()){
            return null;
        }
        return conversationIdList.getLast();
    }
}
