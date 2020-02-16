package com.gossip.server.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {

    private  final  Storage storage;

    @Override
    public List<Record> all() {
        return null;
    }

    @Override
    public void add(String value) {
        storage.insert(new Record(value));
    }
}
