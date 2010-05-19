package com.ucla.WANDA.services;

import com.ucla.WANDA.services.ICallBack;

interface ISensorService2 {
	double getKm();
	void registerCallback(ICallBack cb);
	void unregisterCallback(ICallBack cb);
	void stopAcc();
	void startAcc();
}
