package org.apache.arrow.plasma;

import io.netty.buffer.ArrowBuf;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.LargeBuffer;
import io.netty.buffer.PooledByteBufAllocatorL;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.buffer.UnsafeDirectLittleEndian;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by salah on 18/6/6.
 */
public class ArrowBufPlasmaClient extends PlasmaClient {

  public ArrowBufPlasmaClient(String storeSocketName, String managerSocketName, int releaseDelay) {
    super(storeSocketName, managerSocketName, releaseDelay);
  }

  //@Override
  public void put(byte[] objectId, ArrowBuf value, ArrowBuf metadata) {
    super.put(objectId, value.array(), metadata.array());
  }

  //@Override
  public List<ArrowBuf> get(byte[][] objectIds, int timeoutMs, boolean isMetadata) {
    ByteBuffer[][] bufs = PlasmaClientJNI.get(conn, objectIds, timeoutMs);
    assert bufs.length == objectIds.length;

    List<ArrowBuf> ret = new ArrayList<>();
    for (int i = 0; i < bufs.length; i++) {
      ByteBuffer buf = bufs[i][isMetadata ? 1 : 0];
      if (buf == null) {
        ret.add(null);
      } else {
        ArrowBuf arrowBuf = new ArrowBuf(
            null, null, new UnsafeDirectLittleEndian(buf),
            null, null, 0, buf.remaining(), false);
        ret.add(arrowBuf);
      }
    }
    return ret;
  }
}
