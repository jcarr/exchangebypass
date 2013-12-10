package org.flacid.exchangebypass;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.setBooleanField;
import static de.robv.android.xposed.XposedHelpers.setIntField;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XC_MethodHook;

@SuppressWarnings("unused")
public class Xposed implements IXposedHookLoadPackage {
	XC_MethodReplacement xposedReturnTrue = new XC_MethodReplacement () {
		@Override
		protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
			return Boolean.valueOf(true);
		}
	};
	
	XC_MethodReplacement xposedReturnZero = new XC_MethodReplacement () {
		@Override
		protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
			return 0;
		}
	};
	
	XC_MethodHook updatePolicy = new XC_MethodHook() {
		@Override
		protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
			setIntField(param.thisObject, "mPasswordMode", 0);
			setBooleanField(param.thisObject, "mRequireEncryption", false);
			setBooleanField(param.thisObject, "mRequireEncryptionExternal", false);
			setBooleanField(param.thisObject, "mRequireManualSyncWhenRoaming", false);
			setBooleanField(param.thisObject, "mDontAllowCamera", false);
			setBooleanField(param.thisObject, "mDontAllowAttachments", false);
			setBooleanField(param.thisObject, "mDontAllowHtml", false);
		}
		
		@Override
		protected void afterHookedMethod(MethodHookParam param) throws Throwable {
			
		}
	};
	
    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
      	if (lpparam.packageName.equals("com.google.android.exchange") || lpparam.packageName.equals("com.android.exchange")) {
    		findAndHookMethod("com.android.exchange.adapter.ProvisionParser", lpparam.classLoader, "hasSupportablePolicySet", xposedReturnTrue);
    	}
    	
    	if (lpparam.packageName.equals("com.google.android.email") || lpparam.packageName.equals("com.android.email")) {    	
    		findAndHookMethod("com.android.email.SecurityPolicy", lpparam.classLoader, "isActiveAdmin", xposedReturnTrue);
    		findAndHookMethod("com.android.email.SecurityPolicy", lpparam.classLoader, "isActive", "com.android.emailcommon.provider.Policy", xposedReturnTrue);
    		findAndHookMethod("com.android.email.SecurityPolicy", lpparam.classLoader, "getInactiveReasons", "com.android.emailcommon.provider.Policy", xposedReturnZero);
    		findAndHookMethod("com.android.emailcommon.provider.Policy", lpparam.classLoader, "normalize", updatePolicy);
    		findAndHookMethod("com.android.email.SecurityPolicy", lpparam.classLoader, "remoteWipe", de.robv.android.xposed.XC_MethodReplacement.DO_NOTHING);
    	}
    	
    	return;
    }
}