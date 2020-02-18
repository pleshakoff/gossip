package com.gossip.server.exchange;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/gossip",produces = {MediaType.APPLICATION_JSON_VALUE})
@Api(tags="Gossip")
@RequiredArgsConstructor
class ExchangeController {

    private final ExchangeService exchangeService;

    @GetMapping
    @ApiOperation(value = "Gossip pull")
    public ResponseGossipPullDTO gossip(@RequestParam String id, @RequestParam Integer version)
    {
      return exchangeService.gossipPullResponse(id, version);
    }

}
