<template>
  <div>
    <div id="editor"/>
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
          this.imageData = blob
          const storageRef = firebase.storage().ref(`${this.imageData.name}`).put(this.imageData);
          storageRef.on(`state_changed`, snapshot => {
            this.uploadValue = (snapshot.bytesTransferred/snapshot.totalBytes)*100;
          }, err => {
              console.log(err.message)
            },
            () => {this.uploadValue = 100;
              storageRef.snapshot.ref.getDownloadURL().then(url => {
                // this.picture = url
                callback(url, this.imageData.name);
              })
          })
        }
      }
    })
    editor
  }
};
</script>