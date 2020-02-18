package com.gossip.server.storage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/chat",produces = {MediaType.APPLICATION_JSON_VALUE})
@Api(tags="Chat")
@RequiredArgsConstructor
class ChatController {

    private final StorageService storageService;

    @GetMapping
    @ApiOperation(value = "Get all chat")
    public List<Record> all(){
        return storageService.all();
    }

    @PostMapping
    @ApiOperation(value = "Add message to chat")
    public Record add(@RequestBody String value){

        return storageService.add(value);
    }




}
