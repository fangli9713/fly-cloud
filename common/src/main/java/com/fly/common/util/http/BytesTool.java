package com.fly.common.util.http;

import java.nio.ByteBuffer;

/**
 * 
 * @author HangFu
 *
 */
public class BytesTool {
	private static final char[] encodingURi="0123456789_abcdefghijklmnopqrstuvwxyz-ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
	private static byte long7(long x) { return (byte)(x >> 56); }
    private static byte long6(long x) { return (byte)(x >> 48); }
    private static byte long5(long x) { return (byte)(x >> 40); }
    private static byte long4(long x) { return (byte)(x >> 32); }
    private static byte long3(long x) { return (byte)(x >> 24); }
    private static byte long2(long x) { return (byte)(x >> 16); }
    private static byte long1(long x) { return (byte)(x >>  8); }
    private static byte long0(long x) { return (byte)(x >>  0); }
    
    
    private static byte int3(int x) { return (byte)(x >> 24); }
    private static byte int2(int x) { return (byte)(x >> 16); }
    private static byte int1(int x) { return (byte)(x >>  8); }
    private static byte int0(int x) { return (byte)(x >>  0); }
    
    /**
     * 高字节放在第一位,低字节放在最后一位
     * @param x 要转换的长整形
     * @return
     */
    public static byte[] LongToBytes(long x){
    	byte[] b=new byte[8];
    	b[0]=long7(x);
    	b[1]=long6(x);
    	b[2]=long5(x);
    	b[3]=long4(x);
    	b[4]=long3(x);
    	b[5]=long2(x);
    	b[6]=long1(x);
    	b[7]=long0(x);
        return b;
    }
    
 
    
   
    
    /**
     * 高字节放在第一位,底字节放在最后一位
     * @param b 要填充的字节，如果offset+8>b.length,将会抛出数组下标越界异常
     * @param offset,从第几位开始填充
     * @param x 要转换的长整形
     */
    public static void LongToBytes(byte[] b,int offset,long x){
    	b[offset+0]=long7(x);
    	b[offset+1]=long6(x);
    	b[offset+2]=long5(x);
    	b[offset+3]=long4(x);
    	b[offset+4]=long3(x);
    	b[offset+5]=long2(x);
    	b[offset+6]=long1(x);
    	b[offset+7]=long0(x);
    }
    
    /**
     * 字节转换成长整形
     * @param b 高字节放在第一位,底字节放在最后一位,b.length必须>=8
     */
    public static long BytesToLong(byte[] b){
    	return makeLong(b[0], b[1], b[2],b[3],b[4], b[5], b[6], b[7]);
   	}
    
    public static long BytesToLong(byte[] b,int offset){
    	return makeLong(b[offset+0], b[offset+1], b[offset+2],b[offset+3],b[offset+4], b[offset+5], b[offset+6], b[offset+7]);
   	}
    
    /**
     * 字节转换成长整形
     * @param b 高字节放在第一位,底字节放在最后一位,b.length必须>=8
     * @param offset 从指定下标开始
     */
    public static long byteToLong(byte[] b,int offset){
    	return makeLong(b[offset+0], b[offset+1], b[offset+2],b[offset+3],b[offset+4], b[offset+5], b[offset+6], b[offset+7]);
    }
    
    
    public  static long makeLong(byte b7, byte b6, byte b5, byte b4,
			 byte b3, byte b2, byte b1, byte b0){
			
		return ((((long)b7 & 0xff) << 56) |
			(((long)b6 & 0xff) << 48) |
			(((long)b5 & 0xff) << 40) |
			(((long)b4 & 0xff) << 32) |
			(((long)b3 & 0xff) << 24) |
			(((long)b2 & 0xff) << 16) |
			(((long)b1 & 0xff) <<  8) |
			(((long)b0 & 0xff) <<  0));
	}
    
    
    public  static int makeInt(byte b3, byte b2, byte b1, byte b0){
			
		return ((((int)b3 & 0xff) << 24) |
			(((int)b2 & 0xff) << 16) |
			(((int)b1 & 0xff) <<  8) |
			(((int)b0 & 0xff) <<  0));
	}
    
    /**
     * 高字节放在第一位,低字节放在最后一位
     * @param x 要转换的整形
     * @return
     */
    public static byte[] intToBytes(int x){
    	byte[] b=new byte[4];
    	b[0]=int3(x);
    	b[1]=int2(x);
    	b[2]=int1(x);
    	b[3]=int0(x);
    	return b;
    }
    
    /**
     * 字节转换成整形
     * @param b 高字节放在第一位,底字节放在最后一位,b.length必须>=4
     */
    public static int bytesToInt(byte[] b){
    	return makeInt(b[0], b[1], b[2],b[3]);
    }
    
    public static int bytesToInt(byte[] b ,int offset){
    	return makeInt(b[offset], b[offset+1], b[offset+2],b[offset+3]);
    }
    
	/**
	 * 
	 * @param value
	 * @return 返回一个字节的数据 0
	 */
	public static int getUnsignedByteValue(byte value) {
		return value&0xff;
	}
	
	/**
	 * 
	 * @param value
	 * @return 返回一个字节的数据 0
	 */
	public static int getUnsignedShortValue(short value) {
		return value << 16 >>> 16;
	}
	
//	/**
//	 * 有空格
//	 * @param data
//	 * @return
//	 */
//	public static String byte2HexString(ByteBuffer data){
//		StringBuilder inputBuffer = new StringBuilder();
//		String sTemp;
//		while(data.hasRemaining()){
//			byte cChar=data.get();
//		    sTemp = Integer.toHexString(cChar&0xff);
//		    if(sTemp.length()==1){
//		    	inputBuffer.append('0').append(sTemp);
//		    } else 	
//		        inputBuffer.append(sTemp);
//		    inputBuffer.append(' ');
//		    System.out.println(inputBuffer.toString());
//		}
//		return inputBuffer.toString();
//	}
	
	public static String byteBufferToHexString(ByteBuffer data) {
		int pos=data.position();
		StringBuffer strbuf = new StringBuffer(data.remaining()<<1);
		byte b;
        while(data.hasRemaining()){
        	b=data.get();
        	if (((int) b & 0xff) < 0x10)
				strbuf.append('0');
			strbuf.append(Integer.toString((b & 0xff), 16));
        }
        data.position(pos);
		return strbuf.toString();
		
	}
	
	/**
	 * 字节数组转换成十六进制字符串
	 * 
	 * @param buf
	 *            字节数组
	 * @return 十六进制字符串
	 */
	public static String byteArrayToHexString(byte[] buf) {
		return byteArrayToHexString( buf, 0, buf.length);
	}
	
	/**
	 * 字节数组转换成十六进制字符串,中间没有空格
	 * @param buf
	 * @param offset 
	 * @param length
	 * @return
	 */
	public static String byteArrayToHexString(byte buf[], int offset, int length) {
		StringBuffer strbuf = new StringBuffer(length<<1);
		int i;
		int end=length + offset;
		for (i = offset; i < end; i++) {
			if (((int) buf[i] & 0xff) < 0x10)
				strbuf.append('0');
			strbuf.append(Integer.toString((buf[i] & 0xff), 16));
		}
		return strbuf.toString();
	}

	/**
	 * 十六进制字符串转换成字节数组
	 * @param hex 十六进制字符串 ,不能包含空格
     * @return 字节数组
	 */
	public static byte[] hexStringToByteArray(String hex) {
		if (hex == null)
			return null;
		int length= hex.length();
		int byteLength = length>>1;
		byte[] result = new byte[byteLength];
		char[] buffer = new char[2];
		int i = 0;
		for (int pos = 0; pos < length; pos += 2, i++) {
			buffer[0] = hex.charAt(pos);
			buffer[1] = hex.charAt(pos + 1);
			result[i] = (byte) parseInt(buffer, 16);
		}
		return result;
	}
	
	/**
	 * 将字节压缩成字符
	 * @param data
	 * @return
	 */
	public static String compressBytes(byte[] data){
		return compressBytes(data,0,data.length);
	}
	
	/**
	 * 将字节压缩成字符
	 * @param data
	 * @param offset
	 * @param length
	 * @return
	 */
    public static String compressBytes(byte[] data,int offset,int length){

    	if(data==null){
    		return null;
    	}
    	if(length==0){
    		return "";
    	}
    	
    	int end=offset+length;
    	int charLen;
    	int mod1=length%3;
    	if(mod1==1){
    		charLen=((length/3)<<2)+2;
    	}else if(mod1==2){
    		charLen=((length/3)<<2)+3;
    	}else{
    		charLen=(length/3)<<2;
    	}
    	char charBuf[]=new char[charLen];
    	int bufIndex=0;
    	int XFF=0xFF;
    	int X30=0x30;
    	int X3C=0x3C;
    	int X3F=0x3F;
    	int r=0;
    	for(int i=offset;i<end;i++){
    		if(r==0){
    			charBuf[bufIndex++]=encodingURi[(data[i]&XFF)>>2];
    			r=2;
    		}else if(r==2){
    			charBuf[bufIndex++]=encodingURi[((data[i-1]<<4)&X30)|((data[i]&XFF)>>4)];
    			r=4;
    		}else if(r==4){
    			charBuf[bufIndex++]=encodingURi[((data[i-1]<<2)&X3C)|((data[i]&XFF)>>6)];
    			charBuf[bufIndex++]=encodingURi[(data[i])&X3F];
    		    r=0;
    		}
    	}
    	
		if(r>0){
		  if(r==2){
			  charBuf[bufIndex++]=encodingURi[data[end-1]&0x03];
		  }else if(r==4){
			  charBuf[bufIndex++]=encodingURi[data[end-1]&0x0F];
		  }
	    }
		return new String(charBuf);
	}
    
    /**
     * 将压缩后的字符转义成字节
     * @param compresStr
     * @return
     */
    public static byte[] unCompress(String compresStr){
    	if(compresStr==null){
    		return null;
    	}
    	if(compresStr.length()==0){
    		return new byte[0];
    	}
        int mod1=compresStr.length()%4;
        int len;
        if(mod1==0){
        	len=(compresStr.length()/4)*3;
        }else if(mod1==2){
        	len=(compresStr.length()/4)*3+1;
        }else if(mod1==3){
        	len=(compresStr.length()/4)*3+2;
        }else{
        	throw new IllegalArgumentException("ivalid compres string length:"+compresStr); 
        }
        
        byte data[]=new byte[len];
        int v;
        int r=0;
        int dataIndex=0;
        
        for(int i=0;i<compresStr.length();i++){
        	v=getChar64Value(compresStr.charAt(i));
        	if(r==0){
        		data[dataIndex++]=(byte)(v<<2);
        		r=2;
        	}else if(r==2){
        		if(dataIndex<len){
        			data[dataIndex-1]=(byte)(data[dataIndex-1]|(v>>4));
        			data[dataIndex++]=(byte)(v<<4);
        			r=4;
        		}else{
        			data[dataIndex-1]=(byte)(data[dataIndex-1]|v);
        		}
        	}else if(r==4){
        		
        		if(dataIndex<len){
        			data[dataIndex-1]=(byte)(data[dataIndex-1]|(v>>2));
        			data[dataIndex++]=(byte)(v<<6);
        			r=6;
        		}else{
        			data[dataIndex-1]=(byte)(data[dataIndex-1]|v);
        		}
        	}else if(r==6){
        		data[dataIndex-1]=(byte)(data[dataIndex-1]|v);
        		r=0;
        	}
        }
        
        return data;
        
    }
    
    private static int getChar64Value(char v){
    	int z=(int)v;
    	if(z==95){
    		return 10;
    	}else if(z==45){
    		return 37;
    	}else if(z>=48&&z<=57){
    		return z-48;
    	}else if(z>=65&&z<=90){
    		return z-27;
    	}else if(z>=97&&z<=122){
    		return z-86;
    	}else{
    		throw new IllegalArgumentException("ivalid char :"+v);
    	}
    }
    

	/**
	 * 使用第二个参数指定的基数，将字符数组参数解析为有符号的整数。 除了第一个字符可以是用来表示负值的 ASCII 减号 '-'
	 * ('\u002D’)外，字符串中的字符必须都是指定基数的数字（通过 Character.digit(char, int)
	 * 是否返回一个负值确定）。返回得到的整数值。
	 * 
	 * @param ch
	 *            字符数组
	 * @param radix
	 *            解析ch 时使用的基数
	 * @return
	 * @throws NumberFormatException
	 */
	public static int parseInt(char[] ch, int radix)
			throws NumberFormatException {
		if (ch == null) {
			throw new NumberFormatException("null");
		}

		if (radix < Character.MIN_RADIX) {
			throw new NumberFormatException("radix " + radix
					+ " less than Character.MIN_RADIX");
		}

		if (radix > Character.MAX_RADIX) {
			throw new NumberFormatException("radix " + radix
					+ " greater than Character.MAX_RADIX");
		}

		int result = 0;
		boolean negative = false;
		int i = 0, max = ch.length;
		int limit;
		int multmin;
		int digit;

		if (max > 0) {
			if (ch[0] == '-') {
				negative = true;
				limit = Integer.MIN_VALUE;
				i++;
			} else {
				limit = -Integer.MAX_VALUE;
			}
			multmin = limit / radix;
			if (i < max) {
				digit = Character.digit(ch[i++], radix);
				if (digit < 0) {
					throw new NumberFormatException("For input string: \""
							+ new String(ch) + "\"");
				} else {
					result = -digit;
				}
			}
			while (i < max) {
				// Accumulating negatively avoids surprises near MAX_VALUE
				digit = Character.digit(ch[i++], radix);
				if (digit < 0) {
					throw new NumberFormatException("For input string: \""
							+ new String(ch) + "\"");
				}
				if (result < multmin) {
					throw new NumberFormatException("For input string: \""
							+ new String(ch) + "\"");
				}
				result *= radix;
				if (result < limit + digit) {
					throw new NumberFormatException("For input string: \""
							+ new String(ch) + "\"");
				}
				result -= digit;
			}
		} else {
			throw new NumberFormatException("For input string: \""
					+ new String(ch) + "\"");
		}
		if (negative) {
			if (i > 1) {
				return result;
			} else { /* Only got "-" */
				throw new NumberFormatException("For input string: \""
						+ new String(ch) + "\"");
			}
		} else {
			return -result;
		}
	}
}
