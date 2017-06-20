package com.ikags.ipa2pngeditor.pngs;



public class PNGIHDRTrunk extends PNGTrunk
{
  public int m_nWidth;
  public int m_nHeight;

  public PNGIHDRTrunk(int nSize, String szName, byte[] nData, byte[] nCRC)
  {
    super(nSize, szName, nData, nCRC);

    this.m_nWidth = readInt(nData, 0);
    this.m_nHeight = readInt(nData, 4);
  }
}