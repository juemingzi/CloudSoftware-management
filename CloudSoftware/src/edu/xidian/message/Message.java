package edu.xidian.message;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @ClassName: Message
 * @Description: 消息类
 * @author: wangyannan
 * @date: 2014-11-12 上午10:21:06
 */
public class Message implements Serializable {
	
	private static final long serialVersionUID = 803592200362798025L;
	/**
	 * @fieldName: type
	 * @fieldType: MsgType
	 * @Description: 消息类型
	 */
	private MsgType type;	
	/**
	 * @fieldName: opID
	 * @fieldType: String
	 * @Description: 操作ID
	 */
	private String opID;
	
	/**
	 * @fieldName: values
	 * @fieldType: Object
	 * @Description: 参数列表或者状态
	 */
	private Object values;	
	
	/** 
	 * @Title:Message
	 * @Description:消息构造方法 
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
	 * @Description: 获取消息类型
	 * @return: MsgType
	 */
	public MsgType getType() {
		return type;
	}
	
	/** 
	 * @Title: setType 
	 * @Description: 设置消息类型
	 * @param t
	 * @return: void
	 */
	public void setType(MsgType t) {
		this.type = t;
	}
	
	
	/** 
	 * @Title: getopID 
	 * @Description: 获取操作ID
	 * @return: String
	 */
	public String getopID() {
		return opID;
	}
	
	
	/** 
	 * @Title: getValues 
	 * @Description: 获取参数列表或者状态
	 * @return: Object
	 */
	public Object getValues(){
		return values;
	}

}
