(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-0de9ebec"],{"013d":function(t,e,a){"use strict";a("a408")},"0fd9":function(t,e,a){"use strict";a("4b85");var s=a("2b0e"),n=a("d9f7"),i=a("80d2");const r=["sm","md","lg","xl"],o=["start","end","center"];function l(t,e){return r.reduce((a,s)=>(a[t+Object(i["D"])(s)]=e(),a),{})}const d=t=>[...o,"baseline","stretch"].includes(t),c=l("align",()=>({type:String,default:null,validator:d})),u=t=>[...o,"space-between","space-around"].includes(t),f=l("justify",()=>({type:String,default:null,validator:u})),h=t=>[...o,"space-between","space-around","stretch"].includes(t),g=l("alignContent",()=>({type:String,default:null,validator:h})),p={align:Object.keys(c),justify:Object.keys(f),alignContent:Object.keys(g)},v={align:"align",justify:"justify",alignContent:"align-content"};function b(t,e,a){let s=v[t];if(null!=a){if(e){const a=e.replace(t,"");s+="-"+a}return s+="-"+a,s.toLowerCase()}}const m=new Map;e["a"]=s["a"].extend({name:"v-row",functional:!0,props:{tag:{type:String,default:"div"},dense:Boolean,noGutters:Boolean,align:{type:String,default:null,validator:d},...c,justify:{type:String,default:null,validator:u},...f,alignContent:{type:String,default:null,validator:h},...g},render(t,{props:e,data:a,children:s}){let i="";for(const n in e)i+=String(e[n]);let r=m.get(i);if(!r){let t;for(t in r=[],p)p[t].forEach(a=>{const s=e[a],n=b(t,a,s);n&&r.push(n)});r.push({"no-gutters":e.noGutters,"row--dense":e.dense,["align-"+e.align]:e.align,["justify-"+e.justify]:e.justify,["align-content-"+e.alignContent]:e.alignContent}),m.set(i,r)}return t(e.tag,Object(n["a"])(a,{staticClass:"row",class:r}),s)}})},"1f4f":function(t,e,a){"use strict";a("8b37");var s=a("80d2"),n=a("7560"),i=a("58df");e["a"]=Object(i["a"])(n["a"]).extend({name:"v-simple-table",props:{dense:Boolean,fixedHeader:Boolean,height:[Number,String]},computed:{classes(){return{"v-data-table--dense":this.dense,"v-data-table--fixed-height":!!this.height&&!this.fixedHeader,"v-data-table--fixed-header":this.fixedHeader,"v-data-table--has-top":!!this.$slots.top,"v-data-table--has-bottom":!!this.$slots.bottom,...this.themeClasses}}},methods:{genWrapper(){return this.$slots.wrapper||this.$createElement("div",{staticClass:"v-data-table__wrapper",style:{height:Object(s["g"])(this.height)}},[this.$createElement("table",this.$slots.default)])}},render(t){return t("div",{staticClass:"v-data-table",class:this.classes},[this.$slots.top,this.genWrapper(),this.$slots.bottom])}})},"20f6":function(t,e,a){},"3cac":function(t,e,a){"use strict";a.r(e);var s=function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("v-container",{attrs:{id:"upgrade",fluid:"",tag:"section"}},[a("v-row",{attrs:{justify:"center"}},[a("v-col",{attrs:{cols:"12",md:"8"}},[a("base-material-card",{attrs:{color:"primary"},scopedSlots:t._u([{key:"heading",fn:function(){return[a("div",{staticClass:"display-2 font-weight-light"},[t._v(" Vuetify Material Dashboard ")]),a("div",{staticClass:"subtitle-1 font-weight-light"},[t._v(" Are you looking for more components? Please check our Premium Version of Vuetify Material Dashboard ")])]},proxy:!0}])},[a("v-simple-table",[a("thead",[a("tr",[a("th"),a("th",{staticClass:"subheading font-weight-light text-center"},[t._v(" Free ")]),a("th",{staticClass:"subheading font-weight-light text-center"},[t._v(" PRO ")])])]),a("tbody",{staticClass:"text-center"},[a("tr",[a("th",{staticClass:"text-left font-weight-light subtitle-1"},[t._v(" Components ")]),a("td",[t._v("60")]),a("td",[t._v("200")])]),a("tr",[a("th",{staticClass:"text-left font-weight-light subtitle-1"},[t._v(" Plugins ")]),a("td",[t._v("2")]),a("td",[t._v("4")])]),a("tr",[a("th",{staticClass:"text-left font-weight-light subtitle-1"},[t._v(" Example Pages ")]),a("td",[t._v("3")]),a("td",[t._v("8")])]),a("tr",[a("th",{staticClass:"text-left font-weight-light subtitle-1"},[t._v(" Login, Register, Pricing, Lock Pages ")]),a("td",[a("v-icon",{attrs:{color:"error"}},[t._v(" mdi-close ")])],1),a("td",[a("v-icon",{attrs:{color:"success"}},[t._v(" mdi-check ")])],1)]),a("tr",[a("th",{staticClass:"text-left font-weight-light subtitle-1"},[t._v(" Premium Support ")]),a("td",[a("v-icon",{attrs:{color:"error"}},[t._v(" mdi-close ")])],1),a("td",[a("v-icon",{attrs:{color:"success"}},[t._v(" mdi-check ")])],1)]),a("tr",[a("th"),a("td",[t._v("Free")]),a("td",[t._v("Just for "),a("b",{staticClass:"subheading"},[t._v("$79")])])]),a("tr",[a("th"),a("td",[a("v-btn",{attrs:{color:"grey",disabled:""}},[t._v(" Current Version ")])],1),a("td",[a("v-btn",{attrs:{color:"success",target:"_blank",href:"https://www.creative-tim.com/product/vuetify-material-dashboard-pro?ref=vtymd-upgrade-page"}},[t._v(" Upgrade to Pro ")])],1)])])])],1)],1)],1)],1)},n=[],i={name:"DashboardDashboard"},r=i,o=(a("013d"),a("2877")),l=a("6544"),d=a.n(l),c=a("8336"),u=a("62ad"),f=a("a523"),h=a("132d"),g=a("0fd9"),p=a("1f4f"),v=Object(o["a"])(r,s,n,!1,null,null,null);e["default"]=v.exports;d()(v,{VBtn:c["a"],VCol:u["a"],VContainer:f["a"],VIcon:h["a"],VRow:g["a"],VSimpleTable:p["a"]})},"8b37":function(t,e,a){},a408:function(t,e,a){},a523:function(t,e,a){"use strict";a("20f6"),a("4b85");var s=a("e8f2"),n=a("d9f7");e["a"]=Object(s["a"])("container").extend({name:"v-container",functional:!0,props:{id:String,tag:{type:String,default:"div"},fluid:{type:Boolean,default:!1}},render(t,{props:e,data:a,children:s}){let i;const{attrs:r}=a;return r&&(a.attrs={},i=Object.keys(r).filter(t=>{if("slot"===t)return!1;const e=r[t];return t.startsWith("data-")?(a.attrs[t]=e,!1):e||"string"===typeof e})),e.id&&(a.domProps=a.domProps||{},a.domProps.id=e.id),t(e.tag,Object(n["a"])(a,{staticClass:"container",class:Array({"container--fluid":e.fluid}).concat(i||[])}),s)}})},e8f2:function(t,e,a){"use strict";a.d(e,"a",(function(){return n}));var s=a("2b0e");function n(t){return s["a"].extend({name:"v-"+t,functional:!0,props:{id:String,tag:{type:String,default:"div"}},render(e,{props:a,data:s,children:n}){s.staticClass=`${t} ${s.staticClass||""}`.trim();const{attrs:i}=s;if(i){s.attrs={};const t=Object.keys(i).filter(t=>{if("slot"===t)return!1;const e=i[t];return t.startsWith("data-")?(s.attrs[t]=e,!1):e||"string"===typeof e});t.length&&(s.staticClass+=" "+t.join(" "))}return a.id&&(s.domProps=s.domProps||{},s.domProps.id=a.id),e(a.tag,s,n)}})}}}]);
//# sourceMappingURL=chunk-0de9ebec.d3502e65.js.map