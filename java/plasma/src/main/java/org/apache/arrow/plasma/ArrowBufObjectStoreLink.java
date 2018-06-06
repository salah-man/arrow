/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.arrow.plasma;


import io.netty.buffer.ArrowBuf;
import java.util.List;

public interface ArrowBufObjectStoreLink {

  /**
   * Put value in the local plasma store with object ID <tt>objectId</tt>.
   *
   * @param objectId The object ID of the value to be put.
   * @param value The value to put in the object store.
   * @param metadata encodes whatever metadata the user wishes to encode.
   */
  void put(byte[] objectId, ArrowBuf value, ArrowBuf metadata);

  /**
   * Create a buffer from the PlasmaStore based on the <tt>objectId</tt>.
   *
   * @param objectId The object ID used to identify the object.
   * @param timeoutMs The number of milliseconds that the get call should block before timing out
   * and returning. Pass -1 if the call should block and 0 if the call should return immediately.
   * @param isMetadata false if get data, otherwise get metadata.
   * @return A PlasmaBuffer wrapping the object.
   */
  default ArrowBuf get(byte[] objectId, int timeoutMs, boolean isMetadata) {
    byte[][] objectIds = {objectId};
    return get(objectIds, timeoutMs, isMetadata).get(0);
  }

  /**
   * Create buffers from the PlasmaStore based on <tt>objectIds</tt>.
   *
   * @param objectIds List of object IDs used to identify some objects.
   * @param timeoutMs The number of milliseconds that the get call should block before timing out
   * and returning. Pass -1 if the call should block and 0 if the call should return immediately.
   * @param isMetadata false if get data, otherwise get metadata.
   * @return List of PlasmaBuffers wrapping objects.
   */
  List<ArrowBuf> get(byte[][] objectIds, int timeoutMs, boolean isMetadata);

}
