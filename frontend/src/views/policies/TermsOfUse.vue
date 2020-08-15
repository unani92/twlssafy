<template>
    <div>
        <TermsOfUse/>
        <Footer/>
    </div>
</template>
<script>
import Footer from "../../components/Footer.vue";
import TermsOfUse from "../../components/policies/TermsOfUse.vue";

export default {
components: {
    Footer,
    TermsOfUse
  },
}
</script>
<style scoped>
    .terms{
        padding-top : 150px;
        margin: 0 5% 5% 5%;
    }
    .terms p{
        font-weight: bold;
    }
</style>