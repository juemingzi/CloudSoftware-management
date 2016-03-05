package edu.xidian.message;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @ClassName: Message
 * @Description: ��Ϣ��
 * @author: wangyannan
 * @date: 2014-11-12 ����10:21:06
 */
public class Message implements Serializable {
	
	private static final long serialVersionUID = 803592200362798025L;
	/**
	 * @fieldName: type
	 * @fieldType: MsgType
	 * @Description: ��Ϣ����
	 */
	private MsgType type;	
	/**
	 * @fieldName: opID
	 * @fieldType: String
	 * @Description: ����ID
	 */
	private String opID;
	
	/**
	 * @fieldName: values
	 * @fieldType: Object
	 * @Description: �����б����״̬
	 */
	private Object values;	
	
	/** 
	 * @Title:Message
	 * @Description:��Ϣ���췽�� 
	 * @param type
	 * @param opID
	 * @param time
	 * @param values 
	 */
	public Message(MsgType type, String opID,Object values) {
		this.type = type;
		this.opID = opID;
		this.values=values;
	}

	/**
	 * @Title: getType
	 * @Description: ��ȡ��Ϣ����
	 * @return: MsgType
	 */
	public MsgType getType() {
		return type;
	}
	
	/** 
	 * @Title: setType 
	 * @Description: ������Ϣ����
	 * @param t
	 * @return: void
	 */
	public void setType(MsgType t) {
		this.type = t;
	}
	
	
	/** 
	 * @Title: getopID 
	 * @Description: ��ȡ����ID
	 * @return: String
	 */
	public String getopID() {
		return opID;
	}
	
	
	/** 
	 * @Title: getValues 
	 * @Description: ��ȡ�����б����״̬
	 * @return: Object
	 */
	public Object getValues(){
		return values;
	}

}
