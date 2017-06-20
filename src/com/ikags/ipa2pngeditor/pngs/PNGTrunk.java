package com.ikags.ipa2pngeditor.pngs;


import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


public class PNGTrunk
{
  protected int m_nSize;
  protected String m_szName;
  protected byte[] m_nData;
  protected byte[] m_nCRC;
  public static int[] crc_table = null;

  public static PNGTrunk generateTrunk(DataInputStream file)
  {
    try
    {
      int nSize = file.readInt();

      byte[] nData = new byte[4];
      file.read(nData);
      String szName = new String(nData);

      byte[] nDataBuffer = new byte[nSize];
      file.read(nDataBuffer);

      byte[] nCRC = new byte[4];
      file.read(nCRC);

      if (szName.equalsIgnoreCase("IHDR")) {
        return new PNGIHDRTrunk(nSize, szName, nDataBuffer, nCRC);
      }
      return new PNGTrunk(nSize, szName, nDataBuffer, nCRC);
    }
    catch (IOException e)
    {
    }
    return null;
  }

  protected PNGTrunk(int nSize, String szName, byte[] nCRC) {
    this.m_nSize = nSize;
    this.m_szName = szName;
    this.m_nCRC = nCRC;
  }

  protected PNGTrunk(int nSize, String szName, byte[] nData, byte[] nCRC) {
    this(nSize, szName, nCRC);
    this.m_nData = nData;
  }

  public int getSize() {
    return this.m_nSize;
  }

  public String getName() {
    return this.m_szName;
  }

  public byte[] getData() {
    return this.m_nData;
  }

  public byte[] getCRC() {
    return this.m_nCRC;
  }

  public void writeToStream(FileOutputStream outStream) throws IOException {
    byte[] nSize = new byte[4];
    nSize[0] = (byte)((this.m_nSize & 0xFF000000) >> 24);
    nSize[1] = (byte)((this.m_nSize & 0xFF0000) >> 16);
    nSize[2] = (byte)((this.m_nSize & 0xFF00) >> 8);
    nSize[3] = (byte)(this.m_nSize & 0xFF);

    outStream.write(nSize);
    outStream.write(this.m_szName.getBytes());
    outStream.write(this.m_nData);
    outStream.write(this.m_nCRC);
  }

  public static void writeInt(byte[] nDes, int nPos, int nVal)
  {
    nDes[nPos] = (byte)((nVal & 0xFF000000) >> 24);
    nDes[(nPos + 1)] = (byte)((nVal & 0xFF0000) >> 16);
    nDes[(nPos + 2)] = (byte)((nVal & 0xFF00) >> 8);
    nDes[(nPos + 3)] = (byte)(nVal & 0xFF);
  }

  public static int readInt(byte[] nDest, int nPos) {
    return (nDest[nPos] & 0xFF) << 24 | (nDest[(nPos + 1)] & 0xFF) << 16 | (nDest[(nPos + 2)] & 0xFF) << 8 | nDest[(nPos + 3)] & 0xFF;
  }

  public static void writeCRC(byte[] nData, int nPos)
  {
    int chunklen = readInt(nData, nPos);
    System.out.println(chunklen);
    int sum = CRCChecksum(nData, nPos + 4, 4 + chunklen) ^ 0xFFFFFFFF;
    System.out.println(sum);
    writeInt(nData, nPos + 8 + chunklen, sum);
  }

  public static int CRCChecksum(byte[] nBuffer, int nOffset, int nLength)
  {
    int c = -1;

    if (crc_table == null)
    {
      crc_table = new int[256];
      for (int mkn = 0; mkn < 256; mkn++) {
        int mkc = mkn;
        for (int mkk = 0; mkk < 8; mkk++) {
          if ((mkc & 0x1) == 1)
            mkc = 0xEDB88320 ^ mkc >>> 1;
          else {
            mkc >>>= 1;
          }
        }
        crc_table[mkn] = mkc;
      }
    }
    for (int n = nOffset; n < nLength + nOffset; n++) {
      c = crc_table[((c ^ nBuffer[n]) & 0xFF)] ^ c >>> 8;
    }
    return c;
  }
}