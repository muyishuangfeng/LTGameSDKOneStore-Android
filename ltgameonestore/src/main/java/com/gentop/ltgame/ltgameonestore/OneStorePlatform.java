package com.gentop.ltgame.ltgameonestore;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.gentop.ltgame.ltgameonestore.uikit.OneStoreActivity;
import com.gentop.ltgame.ltgamesdkcore.common.LTGameOptions;
import com.gentop.ltgame.ltgamesdkcore.common.LTGameSdk;
import com.gentop.ltgame.ltgamesdkcore.common.Target;
import com.gentop.ltgame.ltgamesdkcore.impl.OnRechargeListener;
import com.gentop.ltgame.ltgamesdkcore.model.RechargeObject;
import com.gentop.ltgame.ltgamesdkcore.platform.AbsPlatform;
import com.gentop.ltgame.ltgamesdkcore.platform.IPlatform;
import com.gentop.ltgame.ltgamesdkcore.platform.PlatformFactory;
import com.gentop.ltgame.ltgamesdkcore.uikit.BaseActionActivity;
import com.gentop.ltgame.ltgamesdkcore.util.LTGameUtil;

import java.util.Map;

public class OneStorePlatform extends AbsPlatform {

    private OneStoreHelper mHelper;

    private OneStorePlatform(Context context, String appId, String appKey, int payTest, String publicKey,
                             int selfRequestCode, String sku, String productID, Map<String, Object> params,
                             String payType, int target) {
        super(context, appId, appKey, payTest, publicKey, selfRequestCode, sku, productID, params, payType,
                target);
    }

    @Override
    public void recharge(Activity activity, int target, RechargeObject object, OnRechargeListener listener) {
        mHelper = new OneStoreHelper(activity, object.getPublicKey(), object.getPayTest(), object.getSku(),
                object.getGoodsID(), object.getmGoodsType(), object.getSelfRequestCode(), object.getParams(), listener);
        mHelper.initOneStore(listener);
    }

    @Override
    public void onActivityResult(BaseActionActivity activity, int requestCode, int resultCode, Intent data) {
        mHelper.onActivityResult(requestCode, resultCode, data, mHelper.mRequestCode);
    }

    @Override
    public Class getUIKitClazz() {
        return OneStoreActivity.class;
    }

    @Override
    public void recycle() {
        if (mHelper != null) {
            mHelper.release();
        }
    }

    /**
     * 工厂
     */
    public static class Factory implements PlatformFactory {

        @Override
        public IPlatform create(Context context, int target) {
            IPlatform platform = null;
            LTGameOptions options = LTGameSdk.options();
            if (!LTGameUtil.isAnyEmpty(options.getLtAppId(), options.getLtAppKey(), options.getBaseUrl(),
                    options.getSku(), options.getGoodsID(), options.getmPublicKey(),
                    options.getGoodsType()) && options.getmParams() != null &&
                    options.getmPayTest() != -1 && options.getSelfRequestCode() != -1) {
                platform = new OneStorePlatform(context, options.getLtAppId(), options.getLtAppKey(),
                        options.getmPayTest(), options.getmPublicKey(), options.getSelfRequestCode(),
                        options.getSku(), options.getGoodsID(), options.getmParams(), options.getGoodsType(), target);
            }
            return platform;
        }

        @Override
        public int getPlatformTarget() {
            return Target.PLATFORM_ONE_STORE;
        }

        @Override
        public boolean checkLoginPlatformTarget(int target) {
            return false;
        }

        @Override
        public boolean checkRechargePlatformTarget(int target) {
            return target == Target.RECHARGE_ONE_STORE;
        }
    }
}
