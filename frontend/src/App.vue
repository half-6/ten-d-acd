<template>
  <div id="app">
    <div class="container-fluid header">
      <div class="row bg-primary">
        <div class="col">
          <h1 class="text-center">Automatic Thyroid Cancer Recognition System</h1>
          <ul class="nav">
            <li class="nav-item">
              <router-link to="/" class="nav-link active">Home</router-link>
            </li>
            <li class="nav-item">
              <router-link to="/about" class="nav-link">About</router-link>
            </li>
            <li class="nav-item">
              <router-link to="/list" class="nav-link">List View</router-link>
            </li>
          </ul>
        </div>
      </div>
    </div>
    <router-view></router-view>
  </div>
</template>

<script>
export default {
  name: "ultersound-diagnosis"
};
</script>
