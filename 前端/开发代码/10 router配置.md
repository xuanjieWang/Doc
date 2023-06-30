```
import Vue from "vue";
import VueRouter from "vue-router";
import City from '../view/city'
import CityAdd from '../view/city/add'
import Space from '../view/space'
import SpaceAdd from '../view/space/add'
import Main from '../view/main.vue'

Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        component: Main,
        redirect: '/space',
        children: [
            { path: '/space', component: Space },
            { path: '/space/add', component: SpaceAdd },
            { path: '/city', component: City },
            { path: '/city/add', component: CityAdd },
        ]
    },
]
const router = new VueRouter({
    routes
})
export default router

```

## vue+ts
```
 
```
