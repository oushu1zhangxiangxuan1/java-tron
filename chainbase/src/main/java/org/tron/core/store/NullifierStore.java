package org.tron.core.store;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.tron.core.capsule.BytesCapsule;
import org.tron.core.db.TronStoreWithRevoking;
import org.tron.protos.Protocol;

@Component
public class NullifierStore extends TronStoreWithRevoking<BytesCapsule, Protocol.ByteArray> {

  @Autowired
  public NullifierStore(@Value("nullifier") String dbName) {
    super(dbName);
  }

  public void put(BytesCapsule bytesCapsule) {
    put(bytesCapsule.getData(), new BytesCapsule(bytesCapsule.getData()));
  }

  @Override
  public BytesCapsule get(byte[] key) {
    Protocol.ByteArray value = revokingDB.getUnchecked(key);
    if (value == null
        || value == Protocol.ByteArray.getDefaultInstance()
        || value.getData().isEmpty()) {
      return null;
    }
    return new BytesCapsule(value);
  }

  @Override
  public boolean has(byte[] key) {
    return revokingDB.has(key);
  }
}