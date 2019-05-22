import Vue from "vue";
import api from '@/api/image'
import defaultImage from '../assets/i/default-thumbnail.png';
const cache = [];
function setImgSrc(el,binding){
    el.src = defaultImage;
    if (binding.oldValue === undefined || binding.value !== binding.oldValue) {
        const imageUrl = binding.value;
        loadImage(imageUrl)
            .then(resp=>{
                el.src = resp;
            }).catch(err=>{
            el.src = defaultImage;
        })
    }
}
async function loadImage(imageUrl){
    if(!cache[imageUrl]){
        const resp = await api.getImage(imageUrl);
        const mimeType = resp.headers['content-type'].toLowerCase();
        const imgBase64 = new Buffer(resp.data, 'binary').toString('base64');
        cache[imageUrl] =  'data:' + mimeType + ';base64,' + imgBase64;
    }
    return cache[imageUrl]
}

Vue.directive('auth-image', {
    // When the bound element is inserted into the DOM...
    bind: setImgSrc,
    componentUpdated: setImgSrc
})

export default {
    loadImage,
}