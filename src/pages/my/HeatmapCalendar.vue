// HeatmapCalendar.vue
<template>
  <view class="calendar">
    <view class="calendar-header">
      <text v-for="d in weekdays" :key="d" class="weekday">{{ d }}</text>
    </view>
    <view class="calendar-body">
      <view v-for="(week, w) in weeks" :key="w" class="week-row">
        <view v-for="(day, idx) in week" :key="idx" class="day-cell" :style="{ backgroundColor: colorFor(day) }">
          <text v-if="day">{{ day.day }}</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  name: 'HeatmapCalendar',
  props: { history: Array },
  data() { const now=new Date(); return { year:now.getFullYear(), month:now.getMonth()+1 } },
  computed: {
    weekdays() { return ['S','M','T','W','T','F','S'] },
    dataMap() { return this.history.reduce((m,i)=>{m[i.date]=i.value;return m;}, {}) },
    weeks() {
      const weeks=[];
      const firstDow=new Date(this.year,this.month-1,1).getDay();
      const daysInMonth=new Date(this.year,this.month,0).getDate();
      let week=Array(firstDow).fill(null);
      for(let d=1;d<=daysInMonth;d++){
        const dateStr=`${this.year}-${String(this.month).padStart(2,'0')}-${String(d).padStart(2,'0')}`;
        week.push({day:d,value:this.dataMap[dateStr]||0});
        if(week.length===7){weeks.push(week);week=[];}
      }
      if(week.length) weeks.push(week.concat(Array(7-week.length).fill(null)));
      return weeks;
    },
    thresholds(){const vals=this.history.map(i=>i.value),max=vals.length?Math.max(...vals):0;return{t1:max*0.25,t2:max*0.5,t3:max*0.75};}
  },
  methods:{
    colorFor(day){if(!day)return'#FFFFFF00';const v=day.value,{t1,t2,t3}=this.thresholds;if(v===0)return'#EEEEEE';if(v<=t1)return'#D6E685';if(v<=t2)return'#8CC665';if(v<=t3)return'#44A340';return'#1E6823';}
  }
}
</script>

<style scoped>
.calendar{background:#fff;padding:16rpx;border-radius:12rpx;}
.calendar-header{display:flex;}
.weekday{flex:1;text-align:center;font-size:20rpx;color:#888;}
.calendar-body{display:flex;flex-direction:column;margin-top:8rpx;}
.week-row{display:flex;}
.day-cell{flex:1;margin:4rpx;width:40rpx;height:40rpx;border-radius:4rpx;display:flex;align-items:center;justify-content:center;}
.day-cell text{font-size:18rpx;color:#333;}
</style>
