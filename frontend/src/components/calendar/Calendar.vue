<template>
  <section>
    <div class="form-group">
      <div class="input-group" style="text-align : left">
         <flat-pickr
          v-if="getEmail"
          v-model="date"
          :config="config"
          class="form"
          placeholder="Select Date"
          name="date"
        >
        </flat-pickr>
        <button class="btn" type="button" title="Toggle" data-toggle>
          <i class="fa fa-calendar">
            <span aria-hidden="true" class="sr-only"></span>
          </i>
        </button>
        <button class="btn" type="button" title="Clear" @click="myPage">
          <i class="fa fa-times">
            <span aria-hidden="true" class="sr-only">Clear</span>
          </i>               
        </button>
      </div>
    </div>
  </section>
</template>
 
<script>
  // import 'bootstrap/dist/css/bootstrap.css';
  import flatPickr from 'vue-flatpickr-component';  
  import 'flatpickr/dist/flatpickr.css';
  import 'flatpickr/dist/themes/material_blue.css';
  import { Korean } from 'flatpickr/dist/l10n/ko.js';
  import http from '../../api/http-common.js';
  import { calender, getCreateDate } from "../../api";

  export default {
    name: 'Calendar',
    data () {
      return {
        dateList : [],
        date: new Date(),
        calendarEmail : '',
        config: {
          wrap: true,
          dateFormat: 'Y-m-d',
          locale: Korean,
          onChange: (e) => (this.onChange(e)),
          enable: [],
        },
        page : 0,                
      }
    },
    methods : {
      onChange (date) {
          let tmp = date.toString().split(" ");
          let month = tmp[1];

          switch (month) {
            case "Jan":  month = "01";
                     break;
            case "Feb":  month = "02";
                     break;
            case "Mar":  month = "03";
                     break;
            case "Apr":  month = "04";
                     break;
            case "May":  month = "05";
                     break;
            case "Jun":  month = "06";
                     break;
            case "Jul":  month = "07";
                     break;
            case "Aug":  month = "08";
                     break;
            case "Sep":  month = "09";
                     break;
            case "Oct": month = "10";
                     break;
            case "Nov": month = "11";
                     break;
            case "Dec": month = "12";
                     break;
            default: month = "Invalid month";
                     break;
        }

          let day = tmp[2];
          let year = tmp[3];


          const targetDate = year+"-"+month+"-"+day;

          this.getList(targetDate)
          
          },
        getList(targetDate){
          calender(targetDate, this.userInfo.userInfo.email, this.page)
            .then( res => {
               const data = {
                data : res,
                userInfo : this.userInfo,
              }
              this.$router.push({name: "Dummy", params : {data}})
              });
        },
        myPage(){
          this.$router.push({name: "Dummy", params: {userInfo : this.userInfo}})

        }
    },
    computed: {
      getEmail(){
        return this.userInfo.userInfo.email;
      }
    },
    props: {
      userInfo: {
        type: Object,
        required: true,
      },
    },
    components: {
      flatPickr
    },
    created() {
      getCreateDate(this.userInfo.userInfo.nickname)
        .then(res => {this.config.enable = res.data});
    }
  }
</script> 

<style scoped>
.btn {
  background-color: #0095f6;
  border: #0095f6;
  border-radius: 4px;
  padding: 5px 9px;
  color: white;
  margin: 3px;
}
.form {
  border: none;
  background-color: #ffffff00;
  width: 90px;
  text-align: left;
  width : 0.1px;
  outline: none;
}
</style>