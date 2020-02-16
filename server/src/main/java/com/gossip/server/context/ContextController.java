package com.gossip.server.context;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/context",produces = {MediaType.APPLICATION_JSON_VALUE})
@Api(tags="Context")
@RequiredArgsConstructor
class ContextController {

    private final Context context;

    @GetMapping
    @ApiOperation(value = "Get current node meta information")
    public Context getCurrentPeerState()  {
      return context;
    }

    @PostMapping("/stop")
    @ApiOperation(value = "Stop")
    public void stop()  {
        context.setActive(false);
    }

    @PostMapping("/start")
    @ApiOperation(value = "Start")
    public void start()  {
        context.setActive(true);
    }


}
