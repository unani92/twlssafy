<template>
  <section>
    <div class="form-group">
      <div class="input-group">
        <div class="input-group-btn">
          <button class="btn" type="button" title="Toggle" data-toggle>
            <i class="fa fa-calendar">
              <span aria-hidden="true" class="sr-only">Toggle</span>
            </i>
          </button>
          <button class="btn" type="button" title="Clear" data-clear>
            <i class="fa fa-times">
              <span aria-hidden="true" class="sr-only">Clear</span>
            </i>               
          </button>
        </div>
        <flat-pickr
        v-if="getEmail"
          v-model="date"
          :config="config"
          class="form"
          placeholder="Select Date"
          name="date"
        >
        </flat-pickr>
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
  
  export default {
    name: 'Calendar',
    data () {
      return {
        date: new Date(),
        calendarEmail : '',
        config: {
          wrap: true,
          dateFormat: 'Y-m-d',
          locale: Korean,
          onChange: (e) => (this.onChange(e))
        },                
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

          http.get(`/article/date/${targetDate}?email=${this.userInfo.userInfo.email}`)
          .then( res => {
            console.log(res);
            });
          },
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
    mounted() {
      console.log(this.userInfo.userInfo.email)
    }
  }

</script> 

<style scoped>
.btn {
  background-color: #0095f6;
  border: #0095f6;
  border-radius: 4px;
  padding: 5px 9px;
  font-size: 14px;
  color: white;
  margin: 3px;
}
.form {
  border: none;
  background-color: #eeeeee00;
  text-align: right;
  font-size: 16px;
}
</style>