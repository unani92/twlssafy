<template>
    <div>
        <PrivacyPolicy/>
    <Footer/>
    </div>
</template>
<script>
import Footer from "../../components/navigation/Footer.vue";
import PrivacyPolicy from "../../components/policies/PrivacyPolicy.vue";

export default {
components: {
    Footer,
    PrivacyPolicy
  },
}
</script>
<style scoped>
    .policies{
        padding-top : 150px;
        margin: 0 5% 5% 5%;
    }
    p{
        font-weight: bold;
    }
</style>