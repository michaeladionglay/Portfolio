using System;
using System.Diagnostics;

namespace Tetris
{
    public class SpriteFont
    {
        public SpriteFont(string message, int xStart, int yStart)
        {
            this.msg = message;
            this.xStart = xStart;
            this.yStart = yStart;
        }

        public void Update()
        {
            // all the work is done in Draw
        }

        public void Draw()
        {
            String pMsg = this.msg;

            float x = this.xStart;
            float y = this.yStart;

            for (int i = 0; i < pMsg.Length; i++)
            {
                int key = Convert.ToByte(pMsg[i]);

                Glyph pGlyph = GlyphMan.Find(key);
                Debug.Assert(pGlyph != null);

                Azul.Sprite pAzulSprite = new Azul.Sprite(pGlyph.pFont,
                                                            new Azul.Rect(pGlyph.x, pGlyph.y, pGlyph.width, pGlyph.height),
                                                            new Azul.Rect(x, y, pGlyph.width, pGlyph.height),
                                                            new Azul.Color(1.0f, 1.0f, 1.0f));

                pAzulSprite.Update();
                pAzulSprite.Render();

                x += pGlyph.width;
            }
        }

        // Data: ----------------
        string msg;
        int xStart;
        int yStart;
    }
}
