import { configureStore } from "@reduxjs/toolkit";
import jwtReducer from '../store/jwtSlice.js'

export default configureStore({
    reducer: {
        jwt: jwtReducer,
    },
})