<template>
  <div>
    <div id="editor"/>
    <button id="submit">submit</button>
  </div>
</template>
<script>
import 'codemirror/lib/codemirror.css'; // codemirror 스타일
import '@toast-ui/editor/dist/toastui-editor.css'; // Editor's Style
import Editor from '@toast-ui/editor';
import 'highlight.js/styles/github.css'; // code block highlight 스타일
import codeSyntaxHightlight from '@toast-ui/editor-plugin-code-syntax-highlight';
import hljs from 'highlight.js';
import firebase from 'firebase'
export default {
  data() {
    return {
      editorText: 'This is initialValue.',
      editorOptions: {
        hideModeSwitch: true
      },
      imageData: null,
      picture: null,
      uploadValue: 0
    };
  },
  mounted() {
    const editor = new Editor({
      el: document.querySelector("#editor"),
      initialEditType: "markdown",
      previewStyle: "vertical",
      height: "500px",
      plugins: [[codeSyntaxHightlight, { hljs }]],
      hooks: {
        addImageBlobHook: (blob, callback) => {
          const date = new Date()
          this.imageData = blob
          const storageRef = firebase.storage().ref(`${this.imageData.name}_${date.getTime()}`).put(this.imageData);
          storageRef.on(`state_changed`, snapshot => {
            this.uploadValue = (snapshot.bytesTransferred/snapshot.totalBytes)*100;
          }, err => {
              console.log(err.message)
            },
            () => {this.uploadValue = 100;
              storageRef.snapshot.ref.getDownloadURL().then(url => {
                callback(url, `${this.imageData.name}_${date.getTime()}`);
              })
          })
        }
      }
    })
    const btn = document.querySelector("#submit")
    btn.addEventListener("click", () => {
      const editContent = editor.getMarkdown()
      this.$emit("submitContent",editContent)
    })
  }
};
</script>
<style scoped>
  button {
    margin: 1rem 0 1rem 0;
    width: 100px;
    height: 30px;
    border-radius: 3px;
    border-style: none;
    cursor: pointer;
    background-color: #e6837a;
    color: white;
  }
</style>