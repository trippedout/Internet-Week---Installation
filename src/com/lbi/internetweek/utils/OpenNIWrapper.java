package com.lbi.internetweek.utils;

import java.lang.reflect.Method;

import com.lbi.internetweek.model.KinectProxy;

import processing.core.PApplet;
import processing.core.PVector;
import SimpleOpenNI.SimpleOpenNI;

public class OpenNIWrapper extends SimpleOpenNI
{
	protected KinectProxy 		_mediator;
	
	public OpenNIWrapper(PApplet arg0, int arg1)
	{
		super(arg0, arg1);
	}
	
	@Override
	protected void setupCallbackFunc()
	{
		//empty so that setCallbacks works.
	}
	
	public void setCallbacks(KinectProxy kinectMediator)
	{		
		_mediator = kinectMediator;
		
		this._newUserMethod				= null;
		this._lostUserMethod 			= null;
		
		this._startCalibrationMethod 	= null;
		this._endCalibrationMethod		= null;
		
		this._startPoseMethod 			= null;
		this._endPoseMethod				= null;

		this._createHandsMethod			= null;
		this._updateHandsMethod			= null;
		this._destroyHandsMethod		= null;
	
		_newUserMethod = getMethodRef("onNewUser",new Class[] { int.class });
		_lostUserMethod = getMethodRef("onLostUser",new Class[] { int.class });

		// calibrations callbacks
		_startCalibrationMethod = getMethodRef("onStartCalibration",new Class[] { int.class });
		_endCalibrationMethod = getMethodRef("onEndCalibration",new Class[] { int.class, boolean.class });
		
		// pose callbacks
		_startPoseMethod = getMethodRef("onStartPose",new Class[] { String.class,int.class });
		_endPoseMethod = getMethodRef("onEndPose",new Class[] { String.class,int.class });
		
		// hands
		_createHandsMethod = getMethodRef("onCreateHands",new Class[] { int.class,PVector.class,float.class });
		_updateHandsMethod = getMethodRef("onUpdateHands",new Class[] { int.class,PVector.class,float.class });
		_destroyHandsMethod = getMethodRef("onDestroyHands",new Class[] { int.class,float.class });

		// gesture
		_recognizeGestureMethod = getMethodRef("onRecognizeGesture",new Class[] { String.class,PVector.class,PVector.class  });
		_progressGestureMethod = getMethodRef("onProgressGesture",new Class[] { String.class,PVector.class,float.class });

		// nite
		_startSessionMethod = getMethodRef("onStartSession",new Class[] { PVector.class  });
		_endSessionMethod = getMethodRef("onEndSession",new Class[] {});
		_focusSessionMethod = getMethodRef("onFocusSession",new Class[] { String.class,PVector.class,float.class });
	}
	
	@Override
	protected Method getMethodRef(String methodName,Class[] paraList)
	{
		Method	ret = null;
		try {
			ret = _mediator.getClass().getMethod(methodName,paraList);			
		} 
		catch (Exception e) 
		{
			//System.out.println(e);
		}
		return ret;
	}
	
	protected void onNewUserCb(long userId) 
	{
		try {
			_newUserMethod.invoke(_mediator, new Object[] { (int)userId });
		} 
		catch (Exception e) 
		{
		}	
	}

	protected void onLostUserCb(long userId)
	{
		try {
			_lostUserMethod.invoke(_mediator, new Object[] { (int)userId });		
		} 
		catch (Exception e) 
		{
		}	
	}

	protected void onStartCalibrationCb(long userId) 
	{
		try {
			_startCalibrationMethod.invoke(_mediator, new Object[] { (int)userId });	
		} 
		catch (Exception e) 
		{
		}	
	}

	protected void onEndCalibrationCb(long userId, boolean successFlag) 
	{
		try {
			_endCalibrationMethod.invoke(_mediator, new Object[] { (int)userId, successFlag});
		} 
		catch (Exception e) 
		{
		}	
	}

	protected void onStartPoseCb(String strPose, long userId) 
	{
		try {
			_startPoseMethod.invoke(_mediator, new Object[] { strPose,(int)userId });
		} 
		catch (Exception e) 
		{
		}	
	}

	protected void onEndPoseCb(String strPose, long userId)
	{
		try {
			_endPoseMethod.invoke(_mediator, new Object[] { strPose,(int)userId });
		} 
		catch (Exception e) 
		{
		}	
	}

}
