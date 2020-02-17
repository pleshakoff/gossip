package com.gossip.client.context;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/context", produces = {MediaType.APPLICATION_JSON_VALUE})
@Api(tags = "Context")
@RequiredArgsConstructor
class ContextController {

    private final ContextService contextService;

    @GetMapping
    @ApiOperation(value = "Get all peers meta information")
    public List<Context> getCurrentPeerState() {
        return contextService.all();
    }

    @PostMapping("/stop")
    @ApiOperation(value = "Stop")
    public void stop(@RequestParam Integer peerId) {
        contextService.stop(peerId);
    }

    @PostMapping("/start")
    @ApiOperation(value = "Start")
    public void start(@RequestParam Integer peerId) {
        contextService.start(peerId);
    }


}
