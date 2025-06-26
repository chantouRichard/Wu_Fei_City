<template>
  <view style="position: fixed; left: 18px; top: 80px">
    <div style="font-size: 26px; font-weight: bold; font-family: Helvetica">
      请选择登录角色
    </div>
    <div
      style="
        width: 200px;
        white-space: pre-wrap;
        color: #7f7f7f;
        margin-top: 10px;
        font-weight: bold;
        font-family: Helvetica;
      "
    >
      温馨提示：
    </div>
    <div
      style="
        width: 200px;
        font-size: 15px;
        color: #7f7f7f;
        font-weight: bold;
        font-family: Helvetica;
      "
    >
      因在系统中您的账号兼任多个角色，请确认角色后再登录
    </div>
  </view>
  <image src="../../static/login/roleback.png" style="position: fixed;right:0;top:23px;width: 254px;height: 299px;"/>
  <div class="container">
    <div v-for="(role,index) in roles" :key="index" class="button" @click="Navigation(role.route)">

            <image :src="role.image" style="width: 132px;height: 130px;position: relative;top:-13px;"/>
            <div style="font-size: 24px; font-weight: bold; font-family: Helvetica">{{role.name}}</div>
            <image src="../../static/login/arrowright.png" style="width: 10px;height: 16px;"></image>

    </div>
  </div>
</template>

<script>
export default {
    data() {
        return {
            roles:[
                {
                    image: '../../static/login/user.png',
                    route:"login",
                    name: '普通用户'
                },
                {
                    image: '../../static/login/jwh.png',
                    route:"jwhlogin",
                    name: '居委会'
                },
                {
                    image: '../../static/login/admin.png',
                    route:"admin-login",
                    name: '管理员'
                }
            ]
        };
    },
    methods:{
        Navigation(role){
            uni.navigateTo({
                url: `/pages/login/${role}`
            })
        }
    }
};
</script>

<style>
.container{
    width: 100vw;
    height: 100vh;

    display: flex;

    flex-direction: column;

    justify-content: center;
    align-items: center;

    gap: 25px;
}
.button{
    position: relative;
    display: flex;
    justify-content: space-around;
    align-items: center;


    overflow: visible;

    width: 90%;
    height: 102px;

    background-color: #E6E6E6;

    box-shadow: #B3B3B3 4px 4px 18px 0px;

    border-radius: 9px;

    border-color: transparent;
}
</style>
