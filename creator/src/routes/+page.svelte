<script lang="ts">
  import { fromModel, HEIGHT, toModel, WIDTH, type ModelShape } from '$lib/model';

  let model: ModelShape = [...Array(HEIGHT)].map(() => [...Array(WIDTH)].map(() => 1));
  let mode = true;

  let json: HTMLTextAreaElement = null as any;
  let importTextarea: HTMLTextAreaElement = null as any;
  let importModal: HTMLDialogElement = null as any;
</script>

<div class="p-5 flex flex-col lg:flex-row">
  <div class="place-items-center">
    <table>
      {#each { length: HEIGHT } as _, j}
        <tr>
          {#each { length: WIDTH } as _, i}
            <td
              class={`w-5 h-4 border-2 ${model[j][i] ? 'bg-white' : ''}`}
              on:mousedown={() => (mode = !model[j][i])}
              on:mousemove={(e) => {
                if ((e.buttons & 1) === 1) {
                  model[j][i] = mode ? 1 : 0;
                }
              }}
            ></td>
          {/each}
        </tr>
      {/each}
    </table>
  </div>

  <div class="p-5 w-full">
    <button class="btn btn-outline mb-4" on:click={() => importModal.showModal()}
      >Import model</button
    >
    <dialog class="modal" bind:this={importModal}>
      <div class="modal-box">
        <h3 class="text-lg font-bold">Import model</h3>
        <p class="py-4">
          <textarea
            class="textarea textarea-bordered resize-none w-full h-full"
            bind:this={importTextarea}
          />
        </p>
        <div class="modal-action">
          <form method="dialog">
            <button
              class="btn btn-neutral"
              on:click={() => {
                const value = importTextarea.value;
                importTextarea.value = '';
                model = fromModel(JSON.parse(value));
              }}>Import</button
            >
            <button class="btn btn-ghost">Cancel</button>
          </form>
        </div>
      </div>
    </dialog>

    <textarea
      class="textarea textarea-bordered resize-none w-full"
      id="json"
      value={JSON.stringify(toModel(model))}
      bind:this={json}
      on:click={() => json.select()}
      on:focus={() => json.select()}
      readonly
    />
  </div>
</div>

<style lang="scss">
  td {
    user-select: none;
  }

  #json {
    color: var(--text-primary);
    cursor: text;
  }
</style>
